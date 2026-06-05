package com.sprms.registration.api.services;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmDTO.VerifiedUserDTO;

@Service
public class NdiAppUserRepositoryServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiAppUserRepositoryServices.class);
	
	private final Set<String> thidStore = new HashSet<>();

    public boolean existsByThid(String thid) {
    	
    	logger.info("@@@Calling the existsByThid proc.................");
    	
        return thidStore.contains(thid);
    }

    public void save(VerifiedUserDTO user, String thid) {
    	
    	logger.info("@@@Calling the existsByThid proc.................");
    	
        thidStore.add(thid);

        // 👉 Replace this with DB save later
        //System.out.println("Saved user: " + user.getFullName());
    }
}
