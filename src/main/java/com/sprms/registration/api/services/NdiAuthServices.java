package com.sprms.registration.api.services;

import java.util.Objects;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.sprms.registration.frmbean.TokenResponseDTO;

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

	@Value("${app.ndiWebhookToken}")
	private String webhookToken;

	private TokenResponseDTO cachedToken;
	private long expiryTime = 0;

	// caling the repos
	private final WebClient _publicWebClient;

	// this constructor to init the repos
	public NdiAuthServices(@Qualifier("publicWebClient") WebClient publicWebClient,
			@Qualifier("secureWebClient") WebClient secureWebClient) {

		this._publicWebClient = publicWebClient;
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

	// NEW CODE
	public boolean isValidToken(String authHeader) {

		if (authHeader == null || authHeader.isBlank()) {
			return false;
		}

		String token = authHeader.trim();

		// case-insensitive Bearer check
		if (token.toLowerCase().startsWith("bearer ")) {
			token = token.substring(7).trim();
		}

		return webhookToken != null && webhookToken.equals(token);
	}
}
