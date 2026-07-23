package com.sprms.registration.frmbean;

import lombok.Data;

@Data
public class WebhookRegisterRequestDTO {

    private String webhookId;
    private String webhookURL;
    private AuthenticationDTO authentication;
    
	public String getWebhookId() {
		return webhookId;
	}
	public void setWebhookId(String webhookId) {
		this.webhookId = webhookId;
	}
	public String getWebhookURL() {
		return webhookURL;
	}
	public void setWebhookURL(String webhookURL) {
		this.webhookURL = webhookURL;
	}
	public AuthenticationDTO getAuthentication() {
		return authentication;
	}
	public void setAuthentication(AuthenticationDTO authentication) {
		this.authentication = authentication;
	}
    
    
}
