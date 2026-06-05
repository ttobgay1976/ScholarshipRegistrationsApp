package com.sprms.registration.api.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.sprms.registration.frmDTO.VerifiedUserDTO;

@Service
public class WebSocketService {

	// private final declaration
	private final SimpMessagingTemplate template;

	// constructor
	public WebSocketService(SimpMessagingTemplate template) {
		this.template = template;
	}

	public void sendLoginSuccess(String thid, VerifiedUserDTO user) {

		Map<String, Object> data = new HashMap<>();
		data.put("thid", thid);
		data.put("name", user.getFullName());
		data.put("status", "SUCCESS");

		template.convertAndSend("/topic/login/" + thid, data);
	}

	public void sendRedirect(String thid, String redirectUrl) {

        String destination = "/topic/ndi/" + thid;

        Map<String, String> msg = new HashMap<>();
        msg.put("redirect", redirectUrl);

        template.convertAndSend(destination, msg);
	}
}
