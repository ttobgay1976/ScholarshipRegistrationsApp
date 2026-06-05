package com.sprms.registration.api.services;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.AuthDataDTO;
import com.sprms.registration.frmDTO.AuthenticationDTO;
import com.sprms.registration.frmDTO.WebhookRegisterRequestDTO;

@Service
public class WebhookRegistrationService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(WebhookRegistrationService.class);

	// call the Repository
	private final APISchemaConfig _apiSchemaConfig;

	@Autowired
	private RestTemplate restTemplate;

	// constructor
	public WebhookRegistrationService(APISchemaConfig apiSchemaConfig) {
		this._apiSchemaConfig = apiSchemaConfig;

	}

	private final Set<String> registeredTokens = new HashSet<>();
	
	// this method will register the webhook url
	// created 24/04/2026
	public String registerWebhook(String accessToken) {

		// 📦 Build request
		AuthDataDTO authData = new AuthDataDTO();
		authData.setToken("thisisfixtoken01");

		AuthenticationDTO auth = new AuthenticationDTO();
		auth.setType("OAuth2");
		auth.setVersion("v2");
		auth.setData(authData);

		WebhookRegisterRequestDTO request = new WebhookRegisterRequestDTO();
		request.setWebhookId("sprms01");
		request.setWebhookURL("https://growl-corporal-playtime.ngrok-free.dev/webhook");
		request.setAuthentication(auth);

		// 🧾 Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 🔐 IMPORTANT: Pass access token here
		headers.setBearerAuth(accessToken);

		HttpEntity<WebhookRegisterRequestDTO> entity = new HttpEntity<>(request, headers);

		// 🌐 API CALL
		ResponseEntity<String> response = restTemplate.exchange(_apiSchemaConfig.getWebhookRegistrationUrl(),
				HttpMethod.POST, entity, String.class);

		System.out.println("Webhook Register Response: " + response.getBody());

		return response.getBody();
	}

	public boolean isRegistered(String webhookid) {
		return registeredTokens.contains(webhookid);
	}


}
