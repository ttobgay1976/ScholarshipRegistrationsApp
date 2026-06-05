package com.sprms.registration.frmDTO;

import java.time.LocalDateTime;

public class NdiSessionDTO {

	private String threadId;
	private String ndiStatus;
	private String holderDid;
	private String relationshipDid;
	private LocalDateTime createdAt;
	
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getNdiStatus() {
		return ndiStatus;
	}
	public void setNdiStatus(String ndiStatus) {
		this.ndiStatus = ndiStatus;
	}
	public String getHolderDid() {
		return holderDid;
	}
	public void setHolderDid(String holderDid) {
		this.holderDid = holderDid;
	}
	public String getRelationshipDid() {
		return relationshipDid;
	}
	public void setRelationshipDid(String relationshipDid) {
		this.relationshipDid = relationshipDid;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
