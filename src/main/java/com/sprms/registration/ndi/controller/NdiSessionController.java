package com.sprms.registration.ndi.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sprms.registration.api.services.NdiLoginStoreServices;
import com.sprms.registration.api.services.PostLoginService;
import com.sprms.registration.frmbean.VerifiedUserDTO;

import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/ndi")
public class NdiSessionController {
	
	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(NdiSessionController.class);
	
	//CALL REPO
	private final PostLoginService _postLoginService;
	
	//CONSTRUCTOR
	public NdiSessionController(PostLoginService postLoginService) {
		this._postLoginService=postLoginService;
	}
	
    @GetMapping("/session-status")
    public Map<String, Object> sessionStatus(HttpSession session) {

    	logger.info("@@@Calling the sessionStatus proc...................");
    	
        Map<String, Object> res = new HashMap<>();

        String threadId = (String) session.getAttribute("NDI_THREAD_ID");

        if (threadId == null) {
            res.put("authenticated", false);
            return res;
        }

        VerifiedUserDTO user = NdiLoginStoreServices.getUser(threadId);

        if (user != null) {

            // ✅ store actual user object
            session.setAttribute("NDI_USER", user);

            res.put("authenticated", true);
            res.put("user", user); // optional
        } else {
            res.put("authenticated", false);
        }

        return res;
    }
    

}
