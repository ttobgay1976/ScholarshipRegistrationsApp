package com.sprms.registration.api.services;

import org.springframework.boot.web.client.RestTemplateBuilder;
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
import com.sprms.registration.frmDTO.TokenResponseDTO;

@Service
public class BcseaAuthService {

	private final RestTemplate restTemplate;
	private final APISchemaConfig config;

	private TokenResponseDTO cachedToken;
	private long expiryTime;

	public BcseaAuthService(RestTemplateBuilder builder, APISchemaConfig config) {
		this.restTemplate = builder.build();
		this.config = config;
	}

	public synchronized String getAccessToken() {

		if (cachedToken != null && System.currentTimeMillis() < expiryTime) {
			return cachedToken.getAccess_token();
		}

		String url = config.getBcseaTokenUrl();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("grant_type", "client_credentials");
		body.add("client_id", config.getBcseaClientKey());
		body.add("client_secret", config.getBcseaSecretKey());

		HttpEntity<?> request = new HttpEntity<>(body, headers);

		ResponseEntity<TokenResponseDTO> response = restTemplate.exchange(url, HttpMethod.POST, request,
				TokenResponseDTO.class);

		TokenResponseDTO token = response.getBody();

		if (token == null || token.getAccess_token() == null) {
			throw new RuntimeException("Failed to fetch token");
		}

		cachedToken = token;

		int expiresIn = token.getExpires_in() != null ? token.getExpires_in() : 3600;

		expiryTime = System.currentTimeMillis() + ((expiresIn - 30L) * 1000L);

		return token.getAccess_token();
	}
}