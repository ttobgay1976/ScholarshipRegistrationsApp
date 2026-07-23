package com.sprms.registration.api.services;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmbean.AuthDataDTO;
import com.sprms.registration.frmbean.AuthenticationDTO;
import com.sprms.registration.frmbean.WebhookRegisterRequestDTO;

@Service
public class WebhookRegistrationService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(WebhookRegistrationService.class);

	@Value("${app.ndiWebhookToken}")
	private String webhookToken;

	@Value("${ndi.webhook.id}")
	private String webhookId;

	@Value("${ndi.webhook.url}")
	private String webhookUrl;

	// call the Repository
	private final APISchemaConfig _apiSchemaConfig;

	@Autowired
	private RestTemplate restTemplate;

	// constructor
	public WebhookRegistrationService(APISchemaConfig apiSchemaConfig) {
		this._apiSchemaConfig = apiSchemaConfig;

	}

	/**
	 * Temporary local cache. Replace with DB persistence for production.
	 */
	private final Set<String> registeredWebhooks = ConcurrentHashMap.newKeySet();

	public boolean isRegistered(String webhookId) {
		return registeredWebhooks.contains(webhookId);
	}

	public String registerWebhook(String accessToken) {

		validateAccessToken(accessToken);

		// Skip duplicate registration during application runtime
		if (isRegistered(webhookId)) {

			logger.info("@@@Webhook [{}] already registered locally.", webhookId);

			return "@@@Webhook already registered locally.";
		}

		try {

			WebhookRegisterRequestDTO request = buildWebhookRequest();

			HttpHeaders headers = buildHeaders(accessToken);

			HttpEntity<WebhookRegisterRequestDTO> entity = new HttpEntity<>(request, headers);

			ResponseEntity<String> response = restTemplate.exchange(_apiSchemaConfig.getWebhookRegistrationUrl(),
					HttpMethod.POST, entity, String.class);

			registeredWebhooks.add(webhookId);

			logger.info("Webhook registered successfully. WebhookId={}, Status={}", webhookId,
					response.getStatusCode());

			return response.getBody();

		} catch (HttpClientErrorException.Conflict ex) {

			// Provider says webhook already exists
			registeredWebhooks.add(webhookId);

			logger.info("Webhook [{}] already exists on provider side.", webhookId);

			return "Webhook already registered.";

		} catch (HttpClientErrorException ex) {

			logger.error("Webhook registration failed. Status={}, Body={}", ex.getStatusCode(),
					ex.getResponseBodyAsString());

			throw ex;

		} catch (Exception ex) {

			logger.error("Unexpected error during webhook registration.", ex);

			throw new RuntimeException("Unable to register webhook.", ex);
		}
	}

	private WebhookRegisterRequestDTO buildWebhookRequest() {

		AuthDataDTO authData = new AuthDataDTO();
		authData.setToken(webhookToken);

		AuthenticationDTO authentication = new AuthenticationDTO();

		authentication.setType("OAuth2");
		authentication.setVersion("v2");
		authentication.setData(authData);

		WebhookRegisterRequestDTO request = new WebhookRegisterRequestDTO();

		request.setWebhookId(webhookId);
		request.setWebhookURL(webhookUrl);
		request.setAuthentication(authentication);

		return request;
	}

	private HttpHeaders buildHeaders(String accessToken) {

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);

		return headers;
	}

	private void validateAccessToken(String accessToken) {

		if (accessToken == null || accessToken.isBlank()) {
			throw new IllegalArgumentException("Access token cannot be null or empty.");
		}
	}
}
