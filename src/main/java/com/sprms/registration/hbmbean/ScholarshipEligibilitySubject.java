package com.sprms.registration.hbmbean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_scholarship_eligibility_subject")
public class ScholarshipEligibilitySubject {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rule_id")
	private ScholarshipEligibilityRule rule;

	@Column(name = "subject_name", nullable = false)
	private String subjectName;

	@Column(name = "minimum_mark")
	private Double minimumMark;


	@Column(name = "required_subject")
	private Boolean requiredSubject = true;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public ScholarshipEligibilityRule getRule() {
		return rule;
	}


	public void setRule(ScholarshipEligibilityRule rule) {
		this.rule = rule;
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
