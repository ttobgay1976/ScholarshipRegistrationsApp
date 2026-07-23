package com.sprms.registration.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sprms.registration.api.services.BcseaStudentService;

@RestController
@RequestMapping("/student")
public class BcseaRestController {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(BcseaRestController.class);

	// call the REPO
	private final BcseaStudentService _bcseaStudentService;

	// constructor
	public BcseaRestController(BcseaStudentService bcseaStudentService) {
		this._bcseaStudentService = bcseaStudentService;
	}


	@GetMapping("/check-student-type/{indexNumber}")
	public ResponseEntity<Map<String, Object>> checkStudentType(@PathVariable String indexNumber) {

		boolean isRepeater = _bcseaStudentService.isRepeaterStudent(indexNumber);

		return ResponseEntity.ok(Map.of("indexNumber", indexNumber, "repeater", isRepeater));
	}
}
