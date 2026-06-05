package com.sprms.registration.frmDTO;

import lombok.Data;

@Data
public class VerifiedUserDTO {

	private String thid;
    private String idNumber;
    private String fullName;
    private String gender;
    private String dateOfBirth;
    
	public String getIdNumber() {
		return idNumber;
	}
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
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
	public String getThid() {
		return thid;
	}
	public void setThid(String thid) {
		this.thid = thid;
	}
    
    
    
}
