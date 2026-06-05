package com.sprms.registration.utils;

import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class CitizenIdUtil {

	public static String resolveCitizenId(HttpSession session, HttpServletRequest request) {
		
		//this the citizen util to get the CID from the session
		//created 11/05/2026
		
		return Optional.ofNullable((String) session.getAttribute("CID")).orElse(request.getParameter("citizenId"));
	}
}
