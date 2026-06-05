package com.sprms.registration.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "app")
public class APISchemaConfig {

	//THIS IS FOR NDI API
	private String ndiAuthBaseURL;
	private String ndiProofRequestBaseURL;
	private String ndiClientId;
	private String ndiClientSecretId;
	
	private String foundationSchema;
	private String locationSchema;
	private String subcribeWebhookServices;
	private String webhookRegistrationUrl;
	private String unSubcribeWebhookServices;
	
	//this is for the BCSEA API
	private String bcseaApiUrl;
	private String bcseaTokenUrl;
	private String bcseaClientKey;
	private String bcseaSecretKey;
	
	//this is for the BCSEA API
	private String dcrcBaseApiUrl;
	private String dcrcTokenUrl;
	private String dcrcClientKey;
	private String dcrcSecretKey;
	
	//GETTER AND SETTER
	public String getNdiAuthBaseURL() {
		return ndiAuthBaseURL;
	}
	public void setNdiAuthBaseURL(String ndiAuthBaseURL) {
		this.ndiAuthBaseURL = ndiAuthBaseURL;
	}

	public String getNdiProofRequestBaseURL() {
		return ndiProofRequestBaseURL;
	}
	public void setNdiProofRequestBaseURL(String ndiProofRequestBaseURL) {
		this.ndiProofRequestBaseURL = ndiProofRequestBaseURL;
	}
	public String getNdiClientId() {
		return ndiClientId;
	}
	public void setNdiClientId(String ndiClientId) {
		this.ndiClientId = ndiClientId;
	}
	public String getNdiClientSecretId() {
		return ndiClientSecretId;
	}
	public void setNdiClientSecretId(String ndiClientSecretId) {
		this.ndiClientSecretId = ndiClientSecretId;
	}
	public String getFoundationSchema() {
		return foundationSchema;
	}
	public void setFoundationSchema(String foundationSchema) {
		this.foundationSchema = foundationSchema;
	}
	public String getLocationSchema() {
		return locationSchema;
	}
	public void setLocationSchema(String locationSchema) {
		this.locationSchema = locationSchema;
	}
	public String getWebhookRegistrationUrl() {
		return webhookRegistrationUrl;
	}
	public void setWebhookRegistrationUrl(String webhookRegistrationUrl) {
		this.webhookRegistrationUrl = webhookRegistrationUrl;
	}
	public String getSubcribeWebhookServices() {
		return subcribeWebhookServices;
	}
	public void setSubcribeWebhookServices(String subcribeWebhookServices) {
		this.subcribeWebhookServices = subcribeWebhookServices;
	}
	public String getUnSubcribeWebhookServices() {
		return unSubcribeWebhookServices;
	}
	public void setUnSubcribeWebhookServices(String unSubcribeWebhookServices) {
		this.unSubcribeWebhookServices = unSubcribeWebhookServices;
	}
	public String getBcseaApiUrl() {
		return bcseaApiUrl;
	}
	public void setBcseaApiUrl(String bcseaApiUrl) {
		this.bcseaApiUrl = bcseaApiUrl;
	}
	public String getBcseaTokenUrl() {
		return bcseaTokenUrl;
	}
	public void setBcseaTokenUrl(String bcseaTokenUrl) {
		this.bcseaTokenUrl = bcseaTokenUrl;
	}
	public String getBcseaClientKey() {
		return bcseaClientKey;
	}
	public void setBcseaClientKey(String bcseaClientKey) {
		this.bcseaClientKey = bcseaClientKey;
	}
	public String getBcseaSecretKey() {
		return bcseaSecretKey;
	}
	public void setBcseaSecretKey(String bcseaSecretKey) {
		this.bcseaSecretKey = bcseaSecretKey;
	}
	public String getDcrcBaseApiUrl() {
		return dcrcBaseApiUrl;
	}
	public void setDcrcBaseApiUrl(String dcrcBaseApiUrl) {
		this.dcrcBaseApiUrl = dcrcBaseApiUrl;
	}
	public String getDcrcTokenUrl() {
		return dcrcTokenUrl;
	}
	public void setDcrcTokenUrl(String dcrcTokenUrl) {
		this.dcrcTokenUrl = dcrcTokenUrl;
	}
	public String getDcrcClientKey() {
		return dcrcClientKey;
	}
	public void setDcrcClientKey(String dcrcClientKey) {
		this.dcrcClientKey = dcrcClientKey;
	}
	public String getDcrcSecretKey() {
		return dcrcSecretKey;
	}
	public void setDcrcSecretKey(String dcrcSecretKey) {
		this.dcrcSecretKey = dcrcSecretKey;
	}
	
	
	
}
