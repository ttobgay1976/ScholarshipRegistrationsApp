package com.sprms.registration.homepage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//THIS CONTROLLER WILL DISABLE THE ERROR SHOWN IN CONSOLE
//CREATED ON 24/04/2026
//PLACE: YK OFFICE


@Controller
public class FaviconController {

	@RequestMapping("favicon.ico")
	@ResponseBody
	void favicon() {
		// do nothing
	}
}
