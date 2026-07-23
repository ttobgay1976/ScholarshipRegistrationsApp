package com.sprms.registration.api.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprms.registration.api.repository.NdiSessionRepository;
import com.sprms.registration.applicationEnums.NDIStatus;
import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmbean.ProofRequestPayloadDTO;
import com.sprms.registration.frmbean.ProofRequestResponseDTO;
import com.sprms.registration.frmbean.TokenResponseDTO;
import com.sprms.registration.hbmbean.NdiSession;
import com.sprms.registration.utils.DateUtil;

@Service
public class NdiRestServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiRestServices.class);

	private TokenResponseDTO cachedToken;
	private long expiryTime = 0;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper mapper;

	// get the APISchemaConfig
	private final APISchemaConfig _apiSchemaConfig;
	private final NdiWebhookServices _ndiWebhookServices;
	private final NdiSessionRepository _ndiSessionRepository;
	private final WebhookRegistrationService _webhookRegistrationService;

	
	// constructors

	public NdiRestServices(APISchemaConfig apiSchemaConfig, NdiWebhookServices ndiWebhookServices,
			WebhookRegistrationService webhookRegistrationService,NdiSessionRepository ndiSessionRepository) {
		this._apiSchemaConfig = apiSchemaConfig;
		this._ndiWebhookServices = ndiWebhookServices;
		this._ndiSessionRepository=ndiSessionRepository;
		this._webhookRegistrationService=webhookRegistrationService;
	}

	// get the acces token unsing the resttemplate
	// created 22/04/2026
	public synchronized String getAccessToken() {
		logger.info("@@@Calling the getAccessToken proc..................");

		try {

			// 🔥 reuse token if valid
			if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
				return cachedToken.getAccess_token();
			}

			// String url = authBaseUrl + "/authentication/v1/authenticate";
			String url = _apiSchemaConfig.getNdiAuthBaseURL() + "/authentication/v1/authenticate";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "client_credentials");
			body.add("client_id", _apiSchemaConfig.getNdiClientId());
			body.add("client_secret", _apiSchemaConfig.getNdiClientSecretId());

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

			ResponseEntity<TokenResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, request,
					TokenResponseDTO.class);

			cachedToken = response.getBody();

			// 🔥 safety check
			if (cachedToken == null || cachedToken.getAccess_token() == null) {
				throw new RuntimeException("Failed to fetch access token from NDI");
			}

			Integer expiresIn = cachedToken.getExpires_in();
			if (expiresIn == null || expiresIn <= 0) {
				expiresIn = 3600; // fallback 1 hour
			}

			// 🔥 safe expiry calculation
			expiryTime = System.currentTimeMillis() + ((expiresIn - 30L) * 1000L);

			return cachedToken.getAccess_token();

		} catch (HttpClientErrorException e) {

			System.out.println("🔥 ERROR STATUS: " + e.getStatusCode());
			System.out.println("🔥 ERROR BODY: " + e.getResponseBodyAsString());

			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch NDI access token", e);

		} catch (Exception e) {
			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch NDI access token", e);
		}
	}

	// preparing the paylod
	public ProofRequestResponseDTO createProofRequest(String token) {

		logger.info("@@@Calling the createProofRequest proc..................");

		ProofRequestPayloadDTO payload = buildLoginProofRequest();

		String url = _apiSchemaConfig.getNdiProofRequestBaseURL() + "/verifier/v1/proof-request";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token); // IMPORTANT

		HttpEntity<ProofRequestPayloadDTO> request = new HttpEntity<>(payload, headers);

		try {
			ResponseEntity<ProofRequestResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, request,
					ProofRequestResponseDTO.class);

			// REGISTER THE WEBHOOK FOR THE NDI SERVICE
			_webhookRegistrationService.registerWebhook(token);
			
			//save the session soon after the proof request
			NdiSession session = new NdiSession();

			session.setThreadId(response.getBody().getData().getProofRequestThreadId());
			session.setNdiStatus(NDIStatus.PENDING);
			session.setCreatedAt(DateUtil.getCurrentDateTime());

			_ndiSessionRepository.save(session);
	        
			// SUBSCRIBE IMMEDIATELY (THIS IS THE KEY STEP)
			// subscribeToWebhook(response.getBody().getData().getProofRequestThreadId(),
			// "sprms01", token);
			_ndiWebhookServices.webhookSubscribe(response.getBody().getData().getProofRequestThreadId());

			return response.getBody();

		} catch (HttpClientErrorException e) {

			// 🔥 VERY IMPORTANT: show real API error
			System.out.println("🔥 STATUS CODE: " + e.getStatusCode());
			//System.out.println("🔥 RESPONSE BODY: " + e.getResponseBodyAsString());

			throw e;
		}
	}

	// helper class
	public ProofRequestPayloadDTO buildLoginProofRequest() {

		String schema = _apiSchemaConfig.getFoundationSchema();

		return new ProofRequestPayloadDTO("login", "Verify Foundational ID",
				List.of(new ProofRequestPayloadDTO.ProofAttribute("ID Number",
						List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute("Full Name",
								List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute("Gender",
								List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute("Date of Birth",
								List.of(new ProofRequestPayloadDTO.Restriction(schema)))));
	}
	
	//getsession attributes
	public NDIStatus getByThreadId(String thid) {

	    NdiSession entity = _ndiSessionRepository.findByThreadId(thid);

	    if (entity == null) {
	        return NDIStatus.PENDING;
	    }

	    return entity.getNdiStatus();
	}
}
