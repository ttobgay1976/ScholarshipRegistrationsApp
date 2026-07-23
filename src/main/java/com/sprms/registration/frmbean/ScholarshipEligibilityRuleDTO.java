package com.sprms.registration.frmbean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipEligibilityRuleDTO {

	private Long id;

	private String scholarshipName;

	private Integer examYear;

	private Double minimumAverage;

	private Integer bestSubjectCount;

	private Boolean excludeDzongkha;

	private Boolean active;

	private String remarks;

	private LocalDateTime createdAt;

	private String createdBy;

	private LocalDateTime updatedAt;

	private String updatedBy;

	private List<ScholarshipEligibilitySubjectDTO> subjects = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScholarshipName() {
		return scholarshipName;
	}

	public void setScholarshipName(String scholarshipName) {
		this.scholarshipName = scholarshipName;
	}

	public Integer getExamYear() {
		return examYear;
	}

	public void setExamYear(Integer examYear) {
		this.examYear = examYear;
	}

	public Double getMinimumAverage() {
		return minimumAverage;
	}

	public void setMinimumAverage(Double minimumAverage) {
		this.minimumAverage = minimumAverage;
	}

	public Integer getBestSubjectCount() {
		return bestSubjectCount;
	}

	public void setBestSubjectCount(Integer bestSubjectCount) {
		this.bestSubjectCount = bestSubjectCount;
	}

	public Boolean getExcludeDzongkha() {
		return excludeDzongkha;
	}

	public void setExcludeDzongkha(Boolean excludeDzongkha) {
		this.excludeDzongkha = excludeDzongkha;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<ScholarshipEligibilitySubjectDTO> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<ScholarshipEligibilitySubjectDTO> subjects) {
		this.subjects = subjects;
	}
	
	
}
