package com.sprms.registration.api.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmbean.PresentationResultRequestDTO;
import com.sprms.registration.frmbean.RequestedPresentationDTO;
import com.sprms.registration.frmbean.VerifiedUserDTO;

//This is to extract the information from the payload return
//created 25/04/2026
//home Changjiji


@Service
public class NdiDataExtractorServices {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiDataExtractorServices.class);
	
	//This is to extract the information from the payload return
	//created 25/04/2026
	//home Changjiji
	public VerifiedUserDTO extract(PresentationResultRequestDTO payload) {
		
		logger.info("@@@Calling the VerifiedUserDTO extract pro...................");

        RequestedPresentationDTO req = payload.getRequestedPresentation();

        Map<String, Object> revealed = req.getRevealedAttrs();

        VerifiedUserDTO user = new VerifiedUserDTO();

        user.setIdNumber(getValue(revealed, "ID Number"));
        user.setFullName(getValue(revealed, "Full Name"));
        user.setGender(getValue(revealed, "Gender"));
        user.setDateOfBirth(getValue(revealed, "Date of Birth"));

        return user;
    }

	//this is the helper class
    private String getValue(Map<String, Object> revealed, String key) {

        if (revealed == null || !revealed.containsKey(key)) {
            return null;
        }

        Object obj = revealed.get(key);

        if (obj instanceof List<?> list && !list.isEmpty()) {

            Object first = list.get(0);

            if (first instanceof Map<?, ?> map) {
                return String.valueOf(map.get("value"));
            }
        }

        return null;
    }
}
