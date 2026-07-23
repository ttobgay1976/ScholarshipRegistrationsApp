package com.sprms.registration.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//THIS UTIL WAS CREATED TO GET THE CURRENT SYSTEM USER
//CREATED ON DT 13/05/2026
public class GetCurrentUserUtil {

	public static String getCurrentUsername() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return "SYSTEM";
		}

		return authentication.getName();
	}
}
