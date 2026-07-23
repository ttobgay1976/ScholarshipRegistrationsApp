package com.sprms.registration.frmbean;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class StudentProfileDTO {

	private String studentName;
	private String indexNumber;
	private Long cidNumber;
	private String schoolName;
	private String dzongkhag;
	private String stream;
	
	private String streamCode;

	private Integer examYear;
	
	private String type;

    @NotEmpty(message = "Subjects cannot be empty")
    @Valid
	private List<StudentMarksDTO> subjects;
    
	private boolean active;

	private String activationToken;

	private LocalDateTime tokenExpiry;

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public Long getCidNumber() {
		return cidNumber;
	}

	public void setCidNumber(Long cidNumber) {
		this.cidNumber = cidNumber;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getDzongkhag() {
		return dzongkhag;
	}

	public void setDzongkhag(String dzongkhag) {
		this.dzongkhag = dzongkhag;
	}

	public String getStream() {
		return stream;
	}

	public void setStream(String stream) {
		this.stream = stream;
	}

	public List<StudentMarksDTO> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<StudentMarksDTO> subjects) {
		this.subjects = subjects;
	}

	public String getStreamCode() {
		return streamCode;
	}

	public void setStreamCode(String streamCode) {
		this.streamCode = streamCode;
	}


	public Integer getExamYear() {
		return examYear;
	}

	public void setExamYear(Integer examYear) {
		this.examYear = examYear;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

	public LocalDateTime getTokenExpiry() {
		return tokenExpiry;
	}

	public void setTokenExpiry(LocalDateTime tokenExpiry) {
		this.tokenExpiry = tokenExpiry;
	}


	
	
}
