package com.sprms.registration.frmbean;

import java.time.LocalDateTime;


public class NdiLoginAuditDTO {

	private Long id;

	private String thid;

	private String ndiIdNumber;

	private LocalDateTime loginTime;

	private String status; // SUCCESS / FAILED

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThid() {
		return thid;
	}

	public void setThid(String thid) {
		this.thid = thid;
	}

	public String getNdiIdNumber() {
		return ndiIdNumber;
	}

	public void setNdiIdNumber(String ndiIdNumber) {
		this.ndiIdNumber = ndiIdNumber;
	}

	public LocalDateTime getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(LocalDateTime loginTime) {
		this.loginTime = loginTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
