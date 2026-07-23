package com.sprms.registration.api.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.exception.BcseaApiException;
import com.sprms.registration.frmbean.TokenResponseDTO;

@Service
public class BcseaTokenService {

	private final RestTemplate restTemplate;
	private final APISchemaConfig apiSchemaConfig;

	private volatile TokenResponseDTO cachedToken;
	private volatile long expiryTime;

	public BcseaTokenService(RestTemplate restTemplate, APISchemaConfig apiSchemaConfig) {
		this.restTemplate = restTemplate;
		this.apiSchemaConfig = apiSchemaConfig;
	}

	public synchronized String getAccessToken() {

		if (isTokenValid()) {
			return cachedToken.getAccess_token();
		}

		TokenResponseDTO token = fetchToken();

		cachedToken = token;
		expiryTime = calculateExpiry(token.getExpires_in());

		return token.getAccess_token();
	}

	private boolean isTokenValid() {
		return cachedToken != null && cachedToken.getAccess_token() != null && System.currentTimeMillis() < expiryTime;
	}

	private TokenResponseDTO fetchToken() {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");
		body.add("client_id", apiSchemaConfig.getBcseaClientKey());
		body.add("client_secret", apiSchemaConfig.getBcseaSecretKey());

		HttpEntity<?> request = new HttpEntity<>(body, headers);

		ResponseEntity<TokenResponseDTO> response = restTemplate.exchange(apiSchemaConfig.getBcseaTokenUrl(),
				HttpMethod.POST, request, TokenResponseDTO.class);

		TokenResponseDTO token = response.getBody();

		if (token == null || token.getAccess_token() == null) {
			throw new BcseaApiException("Invalid token response");
		}

		return token;
	}

	private long calculateExpiry(Integer expiresIn) {
		int safeExpiry = (expiresIn == null || expiresIn <= 0) ? 3600 : expiresIn;
		return System.currentTimeMillis() + ((safeExpiry - 30L) * 1000L);
	}
}
