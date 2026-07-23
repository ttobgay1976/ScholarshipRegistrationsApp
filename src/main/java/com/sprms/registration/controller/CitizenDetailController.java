package com.sprms.registration.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sprms.registration.DTOMapper.CitizenRegistrationDTOMapper;
import com.sprms.registration.api.services.DcrcAuthNCitizenServices;
import com.sprms.registration.frmbean.CitizenDetailDTO;
import com.sprms.registration.frmbean.ScholarshipRegistrationDTO;

@RestController
@RequestMapping("/service")
public class CitizenDetailController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(CitizenDetailController.class);

	private final DcrcAuthNCitizenServices _dcrcAuthNCitizenServices;
	// constructor
	public CitizenDetailController(DcrcAuthNCitizenServices dcrcAuthNCitizenServices,
			CitizenRegistrationDTOMapper citizenRegistrationDTOMapper) {
		this._dcrcAuthNCitizenServices = dcrcAuthNCitizenServices;
	}

	@GetMapping("/citizen/details")
	public ResponseEntity<?> getCitizenDetails(@RequestParam String cid) {

	    logger.info("@@@ CID: {}", cid);

	    try {
	        String token = _dcrcAuthNCitizenServices.getAccessToken();

	        CitizenDetailDTO dto =
	                _dcrcAuthNCitizenServices.getCitizenDetils(cid, token);

	        if (dto == null) {
	            return ResponseEntity.status(404)
	                    .body(Map.of("error", "Citizen not found"));
	        }

	        return ResponseEntity.ok(dto);

	    } catch (Exception e) {

	        logger.error("DCRC service down", e);

	        return ResponseEntity.status(503)
	                .body(Map.of("error", "DCRC service is currently unavailable"));
	    }
	}
}
