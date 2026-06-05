package com.sprms.registration.hbmbean;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sprms.registration.applicationEnums.ApplicationStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_sch_registration")
public class ScholarshipRegistration {

	@Id
	private Long id;

	@Column(name = "citizen_id")
	private String citizenId;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "middle_name")
	private String middleName;
	@Column(name = "last_name")
	private String lastName;
	private LocalDate dateOfBirth;
	private String gender;
	@Column(name = "father_name")
	private String fatherName;
	@Column(name = "mother_name")
	private String motherName;

	private Integer dzongkhagId;
	@Column(name = "dzongkhag_name")
	private String dzongkhagName;
	private Integer gewogId;
	@Column(name = "gewog_name")
	private String gewogName;
	@Column(name = "village_name")
	private String villageName;
	private String houseNo;
	private String thramNo;
	private String householdNo;

	@Column(name = "guardian_details")
	private String guardianDetail;

	@Column(name = "index_number")
	private String indexNumber;

	@Column(name = "email_id")
	private String emailAddress;

	@Column(name = "contact_no")
	private String contactNo;

	@Column(name = "country_of_completion")
	private String countryOfCompletion;

	@Column(name = "supporting_document")
	private String supportDocuments;
	private String remarks;

	@Enumerated(EnumType.STRING)
	private ApplicationStatus status;

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	private String verified_by;
	private LocalDateTime verified_at;

	private String approved_by;
	private LocalDateTime approved_at;

	// One Flood -> Many Files
	@OneToMany(mappedBy = "scholarshipRegistration", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<SupportingFiles> supportingDocs = new ArrayList<>();

	@OneToOne(mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
	private StudentProfile studentProfile;

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "stream_id") private Stream stream;
	 */

	public void addFile(SupportingFiles file) {
		supportingDocs.add(file);
		file.setScholarshipRegistration(this);
	}

	public void removeFile(SupportingFiles file) {
		supportingDocs.remove(file);
		file.setScholarshipRegistration(null);
	}

	// GETTER AND SETTER
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



	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
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

	public Integer getDzongkhagId() {
		return dzongkhagId;
	}

	public void setDzongkhagId(Integer dzongkhagId) {
		this.dzongkhagId = dzongkhagId;
	}

	public String getDzongkhagName() {
		return dzongkhagName;
	}

	public void setDzongkhagName(String dzongkhagName) {
		this.dzongkhagName = dzongkhagName;
	}

	public Integer getGewogId() {
		return gewogId;
	}

	public void setGewogId(Integer gewogId) {
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

	public String getCountryOfCompletion() {
		return countryOfCompletion;
	}

	public void setCountryOfCompletion(String countryOfCompletion) {
		this.countryOfCompletion = countryOfCompletion;
	}

	public String getSupportDocuments() {
		return supportDocuments;
	}

	public void setSupportDocuments(String supportDocuments) {
		this.supportDocuments = supportDocuments;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public ApplicationStatus getStatus() {
		return status;
	}

	public void setStatus(ApplicationStatus status) {
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

	public List<SupportingFiles> getSupportingDocs() {
		return supportingDocs;
	}

	public void setSupportingDocs(List<SupportingFiles> supportingDocs) {
		this.supportingDocs = supportingDocs;
	}

	public StudentProfile getStudentProfile() {
		return studentProfile;
	}

	public void setStudentProfile(StudentProfile studentProfile) {
		this.studentProfile = studentProfile;
	}

}
