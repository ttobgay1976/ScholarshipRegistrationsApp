package com.sprms.registration.frmbean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StudentApiResponseDTO {

	private StudentsDTO students;

	
	public StudentsDTO getStudents() {
		return students;
	}

	public void setStudents(StudentsDTO students) {
		this.students = students;
	}

	public boolean hasData() {
		return students != null && students.getStudent() != null && !students.getStudent().isEmpty();
	}
}
