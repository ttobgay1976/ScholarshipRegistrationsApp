package com.sprms.registration.api.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.sprms.registration.frmDTO.ProofRequestPayloadDTO;
import com.sprms.registration.frmDTO.ProofRequestResponseDTO;
import com.sprms.registration.frmDTO.TokenResponseDTO;
import com.sprms.registration.hbmbean.NdiSession;
import com.sprms.registration.utils.DateUtil;

@Service
public class NdiRestServices {

	private static final Logger logger = LoggerFactory.getLogger(NdiRestServices.class);

	private TokenResponseDTO cachedToken;
	private long expiryTime = 0;

	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;
	private final APISchemaConfig _apiSchemaConfig;
	private final NdiWebhookServices _ndiWebhookServices;
	private final NdiSessionRepository _ndiSessionRepository;
	private final WebhookRegistrationService _webhookRegistrationService;

	public NdiRestServices(
			APISchemaConfig apiSchemaConfig,
			NdiWebhookServices ndiWebhookServices,
			WebhookRegistrationService webhookRegistrationService,
			NdiSessionRepository ndiSessionRepository,
			RestTemplate restTemplate,
			ObjectMapper mapper) {

		this._apiSchemaConfig = apiSchemaConfig;
		this._ndiWebhookServices = ndiWebhookServices;
		this._webhookRegistrationService = webhookRegistrationService;
		this._ndiSessionRepository = ndiSessionRepository;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}

	public synchronized String getAccessToken() {
		logger.info("@@@Calling the getAccessToken proc..................");

		try {
			if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
				return cachedToken.getAccess_token();
			}

			String url = _apiSchemaConfig.getNdiAuthBaseURL() + "/authentication/v1/authenticate";

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("grant_type", "client_credentials");
			body.add("client_id", _apiSchemaConfig.getNdiClientId());
			body.add("client_secret", _apiSchemaConfig.getNdiClientSecretId());

			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

			ResponseEntity<TokenResponseDTO> response = restTemplate.exchange(
					url,
					HttpMethod.POST,
					request,
					TokenResponseDTO.class);

			cachedToken = response.getBody();

			if (cachedToken == null || cachedToken.getAccess_token() == null) {
				throw new RuntimeException("Failed to fetch access token from NDI");
			}

			Integer expiresIn = cachedToken.getExpires_in();
			if (expiresIn == null || expiresIn <= 0) {
				expiresIn = 3600;
			}

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

	public ProofRequestResponseDTO createProofRequest(String token) {
		logger.info("@@@Calling the createProofRequest proc..................");

		ProofRequestPayloadDTO payload = buildLoginProofRequest();

		String url = _apiSchemaConfig.getNdiProofRequestBaseURL() + "/verifier/v1/proof-request";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<ProofRequestPayloadDTO> request = new HttpEntity<>(payload, headers);

		try {
			ResponseEntity<ProofRequestResponseDTO> response = restTemplate.exchange(
					url,
					HttpMethod.POST,
					request,
					ProofRequestResponseDTO.class);

			if (response.getBody() == null || response.getBody().getData() == null
					|| response.getBody().getData().getProofRequestThreadId() == null) {
				throw new RuntimeException("Invalid proof request response from NDI");
			}

			String threadId = response.getBody().getData().getProofRequestThreadId();

			/*
			 * Register webhook before subscribe.
			 * If webhook already exists, continue.
			 */
			try {
				logger.info("@@@Calling the webhookRegister before subscribe.............");
				_webhookRegistrationService.registerWebhook(token);
			} catch (HttpClientErrorException.Conflict e) {
				logger.warn("@@@Webhook already registered, continuing...");
			}

			NdiSession session = new NdiSession();
			session.setThreadId(threadId);
			session.setNdiStatus(NDIStatus.PENDING);
			session.setCreatedAt(DateUtil.getCurrentDateTime());

			_ndiSessionRepository.save(session);

			_ndiWebhookServices.webhookSubscribe(threadId);

			return response.getBody();

		} catch (HttpClientErrorException e) {
			System.out.println("🔥 STATUS CODE: " + e.getStatusCode());
			System.out.println("🔥 RESPONSE BODY: " + e.getResponseBodyAsString());

			logger.error("@@@NDI API call failed", e);
			throw e;

		} catch (Exception e) {
			logger.error("@@@Failed to create proof request", e);
			throw new RuntimeException("Failed to create proof request", e);
		}
	}

	public ProofRequestPayloadDTO buildLoginProofRequest() {
		String schema = _apiSchemaConfig.getFoundationSchema();

		return new ProofRequestPayloadDTO(
				"login",
				"Verify Foundational ID",
				List.of(
						new ProofRequestPayloadDTO.ProofAttribute(
								"ID Number",
								List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute(
								"Full Name",
								List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute(
								"Gender",
								List.of(new ProofRequestPayloadDTO.Restriction(schema))),
						new ProofRequestPayloadDTO.ProofAttribute(
								"Date of Birth",
								List.of(new ProofRequestPayloadDTO.Restriction(schema)))));
	}

	public NDIStatus getByThreadId(String thid) {
		NdiSession entity = _ndiSessionRepository.findByThreadId(thid);

		if (entity == null) {
			return NDIStatus.PENDING;
		}

		return entity.getNdiStatus();
	}
}
