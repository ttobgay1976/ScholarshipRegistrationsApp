package com.sprms.registration.frmbean;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class ScholarshipRegistrationDTO {

	private Long id;
	private String citizenId;
	private String firstName;
	private String middleName;
	private String lastName;
	private String dateOfBirth;
	private String gender;
	private String fatherName;
	private String motherName;

	private String dzongkhagId;
	private String dzongkhagName;
	private String gewogId;
	private String gewogName;
	private String villageName;
	private String houseNo;
	private String thramNo;
	private String householdNo;
	
	private String guardianDetail;
	private String indexNumber;
	private String emailAddress;
	private String contactNo;
    private Long streamId;
    private String streamName;  // for display only
	private String countryOfCompletion;
	private String remarks;
	private String status;
	
	private String registrationNumber;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	private String verified_by;
	private LocalDateTime verified_at;

    private String approved_by;
    private LocalDateTime approved_at;	
	
	private List<MultipartFile> files;

	// 🔥 One-to-Many DTO relation
	private List<SupportingFilesDTO> supportingdto;
	
	//is student repeater
	private Boolean isStudentRepeater;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getMotherName() {
		return motherName;
	}

	public void setMotherName(String motherName) {
		this.motherName = motherName;
	}

	public String getDzongkhagId() {
		return dzongkhagId;
	}

	public void setDzongkhagId(String dzongkhagId) {
		this.dzongkhagId = dzongkhagId;
	}

	public String getDzongkhagName() {
		return dzongkhagName;
	}

	public void setDzongkhagName(String dzongkhagName) {
		this.dzongkhagName = dzongkhagName;
	}

	public String getGewogId() {
		return gewogId;
	}

	public void setGewogId(String gewogId) {
		this.gewogId = gewogId;
	}

	public String getGewogName() {
		return gewogName;
	}

	public void setGewogName(String gewogName) {
		this.gewogName = gewogName;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

	public String getHouseNo() {
		return houseNo;
	}

	public void setHouseNo(String houseNo) {
		this.houseNo = houseNo;
	}

	public String getThramNo() {
		return thramNo;
	}

	public void setThramNo(String thramNo) {
		this.thramNo = thramNo;
	}

	public String getHouseholdNo() {
		return householdNo;
	}

	public void setHouseholdNo(String householdNo) {
		this.householdNo = householdNo;
	}

	public String getGuardianDetail() {
		return guardianDetail;
	}

	public void setGuardianDetail(String guardianDetail) {
		this.guardianDetail = guardianDetail;
	}

	public String getIndexNumber() {
		return indexNumber;
	}

	public void setIndexNumber(String indexNumber) {
		this.indexNumber = indexNumber;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public Long getStreamId() {
		return streamId;
	}

	public void setStreamId(Long streamId) {
		this.streamId = streamId;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getCountryOfCompletion() {
		return countryOfCompletion;
	}

	public void setCountryOfCompletion(String countryOfCompletion) {
		this.countryOfCompletion = countryOfCompletion;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getVerified_by() {
		return verified_by;
	}

	public void setVerified_by(String verified_by) {
		this.verified_by = verified_by;
	}

	public LocalDateTime getVerified_at() {
		return verified_at;
	}

	public void setVerified_at(LocalDateTime verified_at) {
		this.verified_at = verified_at;
	}

	public String getApproved_by() {
		return approved_by;
	}

	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}

	public LocalDateTime getApproved_at() {
		return approved_at;
	}

	public void setApproved_at(LocalDateTime approved_at) {
		this.approved_at = approved_at;
	}

	public List<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(List<MultipartFile> files) {
		this.files = files;
	}

	public List<SupportingFilesDTO> getSupportingdto() {
		return supportingdto;
	}

	public void setSupportingdto(List<SupportingFilesDTO> supportingdto) {
		this.supportingdto = supportingdto;
	}

	public Boolean getIsStudentRepeater() {
		return isStudentRepeater;
	}

	public void setIsStudentRepeater(Boolean isStudentRepeater) {
		this.isStudentRepeater = isStudentRepeater;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	
	
}
