package com.sprms.registration.ndi.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import com.sprms.registration.api.services.NdiAppUserAuditRepositoryServices;
import com.sprms.registration.api.services.NdiAuthServices;
import com.sprms.registration.api.services.NdiDataExtractorServices;
import com.sprms.registration.api.services.NdiRestServices;
import com.sprms.registration.api.services.NdiWebhookServices;
import com.sprms.registration.frmDTO.NdiSseRegistry;
import com.sprms.registration.frmDTO.PresentationResultRequestDTO;
import com.sprms.registration.frmDTO.ProofRequestResponseDTO;
import com.sprms.registration.ndievent.NdiVerifiedEvent;

@RestController
public class WebhookRestController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiLoginController.class);

	// call the services
	private final NdiRestServices _ndiRestServices;
	private final NdiWebhookServices _ndiWebhookServices;
	private final NdiAuthServices _ndiAuthServices;
	private final ApplicationEventPublisher _publisher;
	@Autowired
    private NdiSseRegistry registry;

	// constructor
	public WebhookRestController(NdiRestServices ndiRestServices, NdiWebhookServices ndiWebhookServices,
			NdiAuthServices ndiAuthServices, NdiAppUserAuditRepositoryServices ndiAppUserAuditRepositoryServices,
			ApplicationEventPublisher applicationEventPublisher,NdiDataExtractorServices ndiDataExtractorServices) {
		this._ndiRestServices = ndiRestServices;
		this._ndiWebhookServices = ndiWebhookServices;
		this._ndiAuthServices = ndiAuthServices;
		this._publisher = applicationEventPublisher;
	}

	@GetMapping("/new-qr")
	@ResponseBody
	public ProofRequestResponseDTO getNewQr() {

		logger.info("@@@Calling the getNewQr proc................");

		String token = _ndiRestServices.getAccessToken();

		ProofRequestResponseDTO response = _ndiRestServices.createProofRequest(token);

		return response;
	}

	//THIS AUTOMATICALLY CALLED BY NGROK
	//FOR PRODUCTION THE URL WILL BE PROVIDED BY GOVTECH AND HAVE TO REPLACE IN THE URL DEFINE CODE
	@PostMapping("/webhook")
	public ResponseEntity<String> webhook(@RequestBody(required = false) PresentationResultRequestDTO payload,
			@RequestHeader(value = "authorization", required = false) String authHeader) {

		System.out.println("=== WEBHOOK HIT ===");

		if (payload == null) {
			System.out.println("Payload is null");
			return ResponseEntity.badRequest().body("Invalid payload");
		}

		// Simple security check (token validation)
		if (!_ndiAuthServices.isValidToken(authHeader)) {
			logger.warn("Unauthorized webhook call");
			return ResponseEntity.status(401).body("Unauthorized");
		}

		// Prevent duplicate processing (THID)
		if (_ndiWebhookServices.isDuplicate(payload.getThid())) {
			logger.warn("Duplicate webhook ignored: {}", payload.getThid());
			return ResponseEntity.ok("Duplicate ignored");
		}

		// Top level checking
		//System.out.println("Type: " + payload.getType());
		//System.out.println("Verification: " + payload.getVerificationResult());
		//System.out.println("THID: " + payload.getThid());
		//System.out.println("Holder DID: " + payload.getHolderDid());

		// Only proceed if proof is valid
		if (!"ProofValidated".equalsIgnoreCase(payload.getVerificationResult())) {
			logger.warn("Proof NOT valid");
			return ResponseEntity.ok("Ignored - Proof not valid");
		}

		// update session immediately before async/event extraction
		_ndiWebhookServices.markSessionProofValidated(payload);

		// fire event ONLY
		_publisher.publishEvent(new NdiVerifiedEvent(payload, payload.getThid()));
		
		 //PUSH EVENT TO FRONTEND
		  registry.send(payload.getThid(), "status", Map.of(
		            "status", "PROOF_VALIDATED"
		    ));

		// only pass to service
		// _ndiWebhookServices.process(payload);
		

		return ResponseEntity.ok("Processed Successfully");
	}

}
