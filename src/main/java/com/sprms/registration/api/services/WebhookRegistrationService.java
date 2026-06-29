package com.sprms.registration.api.services;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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

    private static final Logger logger = LoggerFactory.getLogger(WebhookRegistrationService.class);

    private final APISchemaConfig apiSchemaConfig;
    private final RestTemplate restTemplate;

    @Value("${NDI_WEBHOOK_ID:sprms01}")
    private String webhookId;

    @Value("${NDI_WEBHOOK_URL}")
    private String webhookUrl;

    @Value("${NDI_WEBHOOK_AUTH_TOKEN:thisisfixtoken01}")
    private String webhookAuthToken;

    private final Set<String> registeredTokens = new HashSet<>();

    public WebhookRegistrationService(APISchemaConfig apiSchemaConfig, RestTemplate restTemplate) {
        this.apiSchemaConfig = apiSchemaConfig;
        this.restTemplate = restTemplate;
    }

    public String registerWebhook(String accessToken) {

        logger.info("@@@Calling the webhookRegister.............");
        logger.info("@@@Registering webhookId={}, webhookUrl={}", webhookId, webhookUrl);

        AuthDataDTO authData = new AuthDataDTO();
        authData.setToken(webhookAuthToken);

        AuthenticationDTO auth = new AuthenticationDTO();
        auth.setType("OAuth2");
        auth.setVersion("v2");
        auth.setData(authData);

        WebhookRegisterRequestDTO request = new WebhookRegisterRequestDTO();
        request.setWebhookId(webhookId);
        request.setWebhookURL(webhookUrl);
        request.setAuthentication(auth);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        HttpEntity<WebhookRegisterRequestDTO> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiSchemaConfig.getWebhookRegistrationUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        logger.info("@@@Webhook Register Response: {}", response.getBody());

        registeredTokens.add(webhookId);

        return response.getBody();
    }

    public boolean isRegistered(String webhookid) {
        return registeredTokens.contains(webhookid);
    }
}
