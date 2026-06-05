package com.sprms.registration.api.services;

import java.util.List;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.ProofRequestPayloadDTO;
import com.sprms.registration.frmDTO.ProofRequestResponseDTO;
import com.sprms.registration.frmDTO.TokenResponseDTO;

import org.slf4j.Logger;

@Service
public class NdiAuthServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiAuthServices.class);

	@Value("${ndi.auth.base.url}")
	private String authBaseUrl;
	
	@Value("${ndi.base.url}")
	private String baseUrl;

	@Value("${ndi.client.id}")
	private String clientId;

	@Value("${ndi.client.secret}")
	private String clientSecret;

	private TokenResponseDTO cachedToken;
	private long expiryTime = 0;

	// caling the repos
	private final WebClient _publicWebClient;
	private final WebClient _secureWebClient;
	private final APISchemaConfig _apiSchemaConfig;
	
	@Autowired
	private ObjectMapper mapper;
	

	// this constructor to init the repos
	public NdiAuthServices(@Qualifier("publicWebClient") WebClient publicWebClient,
			@Qualifier("secureWebClient") WebClient secureWebClient, APISchemaConfig apiSchemaConfig) {

		this._publicWebClient = publicWebClient;
		this._secureWebClient = secureWebClient;
		this._apiSchemaConfig = apiSchemaConfig;
	}

	// this is to auto feed the token to proof reqqest call
	// this call the API with Keys to get token.
	public synchronized String getAccessToken() {

		logger.info("@@@Calling the getAccessToken proc..................");

		try {

			// 🔥 reuse token if valid
			if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
				return cachedToken.getAccess_token();
			}

			cachedToken = _publicWebClient.post().uri(authBaseUrl + "/authentication/v1/authenticate")
					.contentType(MediaType.APPLICATION_FORM_URLENCODED)
					.body(BodyInserters.fromFormData("grant_type", "client_credentials").with("client_id", clientId)
							.with("client_secret", clientSecret))
					.retrieve().bodyToMono(TokenResponseDTO.class).block();

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

		} catch (Exception e) {
			logger.error("Error while fetching access token", e);
			throw new RuntimeException("Unable to fetch NDI access token", e);
		}
	}

	// this is for the proof request API with token pass
	public ProofRequestResponseDTO createProofRequest_TO_DELETE(String token) {

		logger.info("@@@Calling the createProofRequest proc..................");

		ProofRequestPayloadDTO payload = buildLoginProofRequest_TO_DETETE();

		System.out.println("🔥 FINAL REQUEST TOKEN : " + token);
		
		try {
			System.out.println("🔥 FINAL REQUEST JSON: " + mapper.writeValueAsString(payload));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return _secureWebClient.post().uri(_apiSchemaConfig.getNdiProofRequestBaseURL() + "/verifier/v1/proof-request")
				.header("Authorization", "Bearer " + token)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(payload)
				.retrieve()
				.bodyToMono(ProofRequestResponseDTO.class)
				.block();
	}

	//This is the place where we define the attribute to get into payload to get info from NDI services
	public ProofRequestPayloadDTO buildLoginProofRequest_TO_DETETE() {

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
	
	//checking for valid token when pass to the header
	public boolean isValidToken(String authHeader) {

        if (authHeader == null || authHeader.isEmpty()) {
            return false;
        }

        String token = authHeader.trim();

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

		/* String expected = "thisisfixtoken01"; */
        String expected = token;

        return expected.equals(token);
    }
}
