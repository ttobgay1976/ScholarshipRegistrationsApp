package com.sprms.registration.api.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sprms.registration.frmDTO.VerifiedUserDTO;

//this will store the verified user details
//created 25/04/2026

@Service
public class NdiLoginStoreServices {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiLoginStoreServices.class);

	  private static final Map<String, VerifiedUserDTO> store = new ConcurrentHashMap<>();

	    public static void saveUser(String threadId, VerifiedUserDTO user) {
	        store.put(threadId, user);
	    }

	    public static VerifiedUserDTO getUser(String threadId) {
	        return store.get(threadId);
	    }

	    public static boolean isVerified(String threadId) {
	        return store.containsKey(threadId);
	    }

	    public static void remove(String threadId) {
	        store.remove(threadId);
	    }
}
