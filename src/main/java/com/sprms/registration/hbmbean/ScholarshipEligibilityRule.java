package com.sprms.registration.hbmbean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_scholarship_eligibility_rule")
public class ScholarshipEligibilityRule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "scholarship_name", nullable = false, length = 100)
	private String scholarshipName;

	@Column(name = "exam_year", nullable = false)
	private Integer examYear;

	@Column(name = "minimum_average")
	private Double minimumAverage;

	@Column(name = "best_subject_count")
	private Integer bestSubjectCount = 0;

	@Column(name = "exclude_dzongkha")
	private Boolean excludeDzongkha = false;

	@Column(name = "is_active")
	private Boolean active = true;


	@Column(name = "remarks", length = 500)
	private String remarks;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by")
	private String updatedBy;

	@OneToMany(mappedBy = "rule", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<ScholarshipEligibilitySubject> subjects = new ArrayList<>();

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

	public List<ScholarshipEligibilitySubject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<ScholarshipEligibilitySubject> subjects) {
		this.subjects = subjects;
	}
	
	
}
