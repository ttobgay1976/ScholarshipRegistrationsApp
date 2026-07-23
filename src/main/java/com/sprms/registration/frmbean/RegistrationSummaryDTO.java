package com.sprms.registration.frmbean;

public class RegistrationSummaryDTO {

	private ScholarshipRegistrationDTO registration;

	private StudentProfileDTO student;

	private EligibilityResultDTO eligibility;

	public ScholarshipRegistrationDTO getRegistration() {
		return registration;
	}

	public void setRegistration(ScholarshipRegistrationDTO registration) {
		this.registration = registration;
	}

	public StudentProfileDTO getStudent() {
		return student;
	}

	public void setStudent(StudentProfileDTO student) {
		this.student = student;
	}

	public EligibilityResultDTO getEligibility() {
		return eligibility;
	}

	public void setEligibility(EligibilityResultDTO eligibility) {
		this.eligibility = eligibility;
	}
}
