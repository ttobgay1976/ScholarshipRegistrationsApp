package com.sprms.registration.frmbean;

import java.time.LocalDateTime;

import lombok.Data;

public class AppUserDTO {

	private Long id;
	private String ndiIdNumber;
	private String fullName;
	private String gender;
	private String dateOfBirth;
	private String role;
	private String ndiStatus;
	private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNdiIdNumber() {
		return ndiIdNumber;
	}

	public void setNdiIdNumber(String ndiIdNumber) {
		this.ndiIdNumber = ndiIdNumber;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getNdiStatus() {
		return ndiStatus;
	}

	public void setNdiStatus(String ndiStatus) {
		this.ndiStatus = ndiStatus;
	}
	
	
}
