package com.sprms.registration.api.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmDTO.VerifiedUserDTO;

//This is created to keep the NDI user Audit log detail
//created 25/04/2026
//place : home

@Service
public class NdiAuditServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiAuditServices.class);
	
	 public void auditLog(String thid, VerifiedUserDTO user) {
		 
		 logger.info("@@@Calling the auditLog proc...............");
		 logger.info("Can save the log for future purpose");
		 
		 
	        System.out.println("===== AUDIT LOG =====");
	        System.out.println("THID: " + thid);
	        System.out.println("User: " + user.getFullName());
	        System.out.println("ID: " + user.getIdNumber());
	    }
	 
}
