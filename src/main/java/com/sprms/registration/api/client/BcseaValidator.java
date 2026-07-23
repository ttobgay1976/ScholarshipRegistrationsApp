package com.sprms.registration.api.client;

import org.springframework.stereotype.Component;

import com.sprms.registration.frmbean.StudentApiResponseDTO;

@Component
public class BcseaValidator {

	public void validateResponse(StudentApiResponseDTO body) {

		if (body == null || body.getStudents() == null || body.getStudents().getStudent() == null
				|| body.getStudents().getStudent().isEmpty()) {
			throw new RuntimeException("Empty BCSEA response");
		}
	}
}