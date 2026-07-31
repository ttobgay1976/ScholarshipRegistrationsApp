package com.sprms.registration.frmbean;

import java.util.List;

//this is use for the preview purpose
//created on dt 31/07/2026
public class RegistrationPreviewDTO {

	private StudentProfileDTO student;

	private List<StudentMarksDTO> marks;

	private EligibilityResultDTO eligibility;

	private boolean repeater;

	public StudentProfileDTO getStudent() {
		return student;
	}

	public void setStudent(StudentProfileDTO student) {
		this.student = student;
	}

	public List<StudentMarksDTO> getMarks() {
		return marks;
	}

	public void setMarks(List<StudentMarksDTO> marks) {
		this.marks = marks;
	}

	public EligibilityResultDTO getEligibility() {
		return eligibility;
	}

	public void setEligibility(EligibilityResultDTO eligibility) {
		this.eligibility = eligibility;
	}

	public boolean isRepeater() {
		return repeater;
	}

	public void setRepeater(boolean repeater) {
		this.repeater = repeater;
	}
	
	

}
