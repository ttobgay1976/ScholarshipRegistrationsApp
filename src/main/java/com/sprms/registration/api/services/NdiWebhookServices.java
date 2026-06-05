package com.sprms.registration.api.services;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sprms.registration.api.repository.NdiAppUserAuditRepository;
import com.sprms.registration.api.repository.NdiAppUserRepository;
import com.sprms.registration.api.repository.NdiSessionRepository;
import com.sprms.registration.api.sessionStore.NdiUserSessionStore;
import com.sprms.registration.applicationEnums.NDIStatus;
import com.sprms.registration.config.APISchemaConfig;
import com.sprms.registration.frmDTO.NdiLoginAuditDTO;
import com.sprms.registration.frmDTO.PresentationResultRequestDTO;
import com.sprms.registration.frmDTO.VerifiedUserDTO;
import com.sprms.registration.hbmbean.AppUser;
import com.sprms.registration.hbmbean.NdiLoginAudit;
import com.sprms.registration.hbmbean.NdiSession;
import com.sprms.registration.ndievent.NdiVerifiedEvent;
import com.sprms.registration.ndievent.NdiVerifiedEvent;
import com.sprms.registration.utils.DateUtil;

import jakarta.servlet.http.HttpSession;

@Service
public class NdiWebhookServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiWebhookServices.class);

	// call the services and repository
	private final NdiAuthServices _ndiAuthServices;
	private final APISchemaConfig _apiSchemaConfig;
	private final NdiDataExtractorServices _ndiDataExtractorServices;
	private final NdiAppUserRepository _ndiAppUserRepository;
	private final NdiAppUserRepositoryServices _ndiWebhookRepositoryServices;
	private final NdiAppUserAuditRepository _ndiAppUserAuditRepository;
	private final NdiSessionRepository _ndiSessionRepository;

	@Autowired
	private RestTemplate restTemplate;

	// constructor to initialize the services and repository
	public NdiWebhookServices(NdiAuthServices ndiAuthServices, APISchemaConfig apiSchemaConfig,
			NdiDataExtractorServices ndiDataExtractorServices, NdiAppUserRepository ndiAppUserRepository,
			NdiAuditServices ndiAuditServices, NdiAppUserRepositoryServices ndiWebhookRepositoryServices,
			NdiAppUserAuditRepositoryServices ndiAppUserAuditRepositoryServices,
			NdiAppUserAuditRepository ndiAppUserAuditRepository, WebSocketService webSocketService,
			NdiUserSessionStore ndiUserSessionStore, NdiSessionRepository ndiSessionRepository) {
		this._ndiAuthServices = ndiAuthServices;
		this._apiSchemaConfig = apiSchemaConfig;
		this._ndiDataExtractorServices = ndiDataExtractorServices;
		this._ndiAppUserRepository = ndiAppUserRepository;
		this._ndiWebhookRepositoryServices = ndiWebhookRepositoryServices;
		this._ndiAppUserAuditRepository = ndiAppUserAuditRepository;
		this._ndiSessionRepository=ndiSessionRepository;
	}

	public void webhookSubscribe(String threadId) {

		logger.info("@@@Calling the webhookSubscribe.............");

		String token = _ndiAuthServices.getAccessToken();

		String url = _apiSchemaConfig.getSubcribeWebhookServices();

		// Request body
		Map<String, String> body = new HashMap<>();
		body.put("webhookId", "sprms01");
		body.put("threadId", threadId);

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token); // same as "Authorization: Bearer ..."
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

			System.out.println("✅ Subscribe response: " + response.getBody());

		} catch (HttpClientErrorException e) {
			System.err.println("❌ Client error: " + e.getStatusCode());
			System.err.println("❌ Response: " + e.getResponseBodyAsString());
			throw e;

		} catch (Exception e) {
			System.err.println("❌ General error: " + e.getMessage());
			throw e;
		}
	}

	// unsubscribe soon after get the information and system login successful
	public void unsubscribe(String threadId) {

		// Get token
		String token = _ndiAuthServices.getAccessToken();

		// String url = "https://demo-client.bhutanndi.com/webhook/v1/unsubscribe";
		String url = _apiSchemaConfig.getUnSubcribeWebhookServices();

		// Request body
		UnsubscribeRequest req = new UnsubscribeRequest(threadId);

		// Headers
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token); // cleaner than "Bearer " + token

		HttpEntity<UnsubscribeRequest> entity = new HttpEntity<>(req, headers);

		// Call API
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

		// Optional logging
		System.out.println("Unsubscribe Response: " + response.getBody());
	}

	// Helper class
	static class SubscribeRequest {
		@JsonProperty("webhookId")
		public String webhookId;
		@JsonProperty("threadId")
		public String threadId;

		SubscribeRequest(String webhookId, String threadId) {
			this.webhookId = webhookId;
			this.threadId = threadId;
		}
	}

	// helper class
	static class UnsubscribeRequest {
		@JsonProperty("threadId")
		public String threadId;

		UnsubscribeRequest(String threadId) {
			this.threadId = threadId;
		}
	}

	// Duplicate check using THID
	public boolean isDuplicate(String thid) {
		return _ndiWebhookRepositoryServices.existsByThid(thid);
	}

	// helper method
	private void saveUser(VerifiedUserDTO user) {

		logger.info("@@@Calling the saveUser proce................");

		AppUser entity = new AppUser();
		entity.setNdiIdNumber(user.getIdNumber());
		entity.setFullName(user.getFullName());
		entity.setGender(user.getGender());
		entity.setDateOfBirth(user.getDateOfBirth());
		entity.setCreatedAt(DateUtil.getCurrentDateTime());
		entity.setNdiStatus(NDIStatus.VERIFIED);

		_ndiAppUserRepository.save(entity);
	}

	private void saveAuditSuccess(String thid, VerifiedUserDTO user) {

		logger.info("@@@Calling the saveAuditSuccess proce................");

		NdiLoginAudit audit = new NdiLoginAudit();
		audit.setThid(thid);
		audit.setNdiIdNumber(user.getIdNumber());
		audit.setLoginTime(DateUtil.getCurrentDateTime());
		audit.setStatus("SUCCESS");

		_ndiAppUserAuditRepository.save(audit);
	}

	// Event driven handler
	// created dat 26/04/2026
	// palce : Home
	@EventListener
	public void handle(NdiVerifiedEvent event) {

		PresentationResultRequestDTO payload = event.getPayload();
		String thid = payload.getThid();

		// 🔐 1. IDENTITY GUARD (IDEMPOTENCY)
		if (_ndiAppUserAuditRepository.existsByThid(thid)) {
			return;
		}

		try {

			//invalid proof
			if (!"ProofValidated".equalsIgnoreCase(payload.getVerificationResult())) {
				return;
			}

			// EXTRACT USER
			VerifiedUserDTO user = _ndiDataExtractorServices.extract(payload);

			// SAVE USER (UPSERT SAFE)
			saveUser(user);

			// AUDIT SUCCESS
			saveAuditSuccess(thid, user);

			//store TEMP mapping for login completion
			NdiLoginStoreServices.saveUser(thid, user);

			//update the session details
			NdiSession session = _ndiSessionRepository.findByThreadId(payload.getThid());

			session.setNdiStatus(NDIStatus.PROOF_VALIDATED);
			session.setHolderDid(payload.getHolderDid());
			session.setRelationshipDid(payload.getRelationshipDid());

			_ndiSessionRepository.save(session);

			// websocket notify UI
			// _webSocketService.sendLoginSuccess(thid, user);

			//unsubscribe last
			unsubscribe(thid);

		} catch (Exception e) {
			logger.info("ERROR", e.getMessage());
		}
	}
}
