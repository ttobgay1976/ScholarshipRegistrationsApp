package com.sprms.registration.homepage.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sprms.registration.frmbean.ApiResponseDTO;
import com.sprms.registration.frmbean.ScholarshipRegistrationDTO;
import com.sprms.registration.frmbean.StreamDTO;

@Controller
public class HomePageController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(HomePageController.class);
		
	
	// setting the static variable for the form call
	private static String DISPLAY_NDI_LOGIN_PAGE = "loginFrm";
	private static String DISPLAY_DASHBOARD_PAGE = "layouts/dashboard";
	private static String DISPLAY_SCHOLARSHIP_REGISTRATION_PAGE = "ScholarshipRegistrationFrm";
	
	
	//get the NDI login page
	//created 20/04/2026
	@GetMapping("/")
	public String getNDIloginFrm(Model model) {
		
		//return the NDI login Page
		
		return DISPLAY_NDI_LOGIN_PAGE;
	}
	
	//calling the Scholarship Registration Page
	//date 08/04/2026
	@RequestMapping("/notuse")
	public String getHomePage(Model model) {
		
		logger.info("@@@Calling the Hope page----------------");
		//add model to registration page
		model.addAttribute("scholarshipRegistrationdto", new ScholarshipRegistrationDTO());
		
		//call the API with response
		
		return DISPLAY_SCHOLARSHIP_REGISTRATION_PAGE;
	}
}
