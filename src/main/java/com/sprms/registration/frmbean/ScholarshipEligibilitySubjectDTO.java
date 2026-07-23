package com.sprms.registration.frmbean;

public class ScholarshipEligibilitySubjectDTO {

	private Long id;

	private Long ruleId;

	private String subjectName;

	private Double minimumMark;

	private Boolean requiredSubject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public Double getMinimumMark() {
		return minimumMark;
	}

	public void setMinimumMark(Double minimumMark) {
		this.minimumMark = minimumMark;
	}

	public Boolean getRequiredSubject() {
		return requiredSubject;
	}

	public void setRequiredSubject(Boolean requiredSubject) {
		this.requiredSubject = requiredSubject;
	}
	
	
}
