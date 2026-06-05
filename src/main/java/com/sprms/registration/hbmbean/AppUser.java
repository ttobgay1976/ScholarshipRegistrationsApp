package com.sprms.registration.hbmbean;

import java.time.LocalDateTime;

import com.sprms.registration.applicationEnums.NDIStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_ndiappuser")
public class AppUser {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ndiIdNumber;   // unique identity from NDI
    private String fullName;
    private String gender;
    private String dateOfBirth;

    private String role = "USER";
    
    @Enumerated(EnumType.STRING)
    private NDIStatus ndiStatus = NDIStatus.PENDING;

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

	public NDIStatus getNdiStatus() {
		return ndiStatus;
	}

	public void setNdiStatus(NDIStatus ndiStatus) {
		this.ndiStatus = ndiStatus;
	}


    
	
    
}
