package com.sprms.registration.hbmbean;

import java.time.LocalDateTime;

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
@Table(name = "tbl_sch_supporting_document")
public class SupportingFiles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "supporting_document_file_name")
	private String fileName;
	@Column(name = "supporting_document_file_location")
	private String fileLocation;
	@Column(name = "created_at", updatable = false, insertable = true)
	private LocalDateTime createdAt;
	@Column(name = "updated_at", updatable = true, insertable = false)
	private LocalDateTime updatedAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sch_registration_id", nullable = true)
	private ScholarshipRegistration scholarshipRegistration;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public ScholarshipRegistration getScholarshipRegistration() {
		return scholarshipRegistration;
	}

	public void setScholarshipRegistration(ScholarshipRegistration scholarshipRegistration) {
		this.scholarshipRegistration = scholarshipRegistration;
	}



	
}
