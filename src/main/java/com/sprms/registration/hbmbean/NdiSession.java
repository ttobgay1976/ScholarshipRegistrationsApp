package com.sprms.registration.hbmbean;

import java.time.LocalDateTime;

import com.sprms.registration.applicationEnums.NDIStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbl_ndisession")
public class NdiSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "thread_id", nullable = false, unique = true)
    private String threadId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ndi_status", nullable = false)
    private NDIStatus ndiStatus;

    @Column(name = "holder_did")
    private String holderDid;

    @Column(name = "relationship_did")
    private String relationshipDid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public NDIStatus getNdiStatus() {
		return ndiStatus;
	}

	public void setNdiStatus(NDIStatus ndiStatus) {
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
