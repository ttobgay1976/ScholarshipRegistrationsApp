package com.sprms.registration.api.services;

import org.springframework.stereotype.Service;

import com.sprms.registration.frmbean.VerifiedUserDTO;

import jakarta.servlet.http.HttpSession;

@Service
public class NdiUserSessionService {

	public VerifiedUserDTO getLoggedUser(HttpSession session) {
		return (VerifiedUserDTO) session.getAttribute("USER");
	}
}
