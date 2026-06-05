package com.sprms.registration.frmDTO;

import jakarta.persistence.Id;

public class StreamDTO {

	@Id
	private Long id;
	private String streamName;
	private String descriptions;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStreamName() {
		return streamName;
	}
	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}
	public String getDescriptions() {
		return descriptions;
	}
	public void setDescriptions(String descriptions) {
		this.descriptions = descriptions;
	}
	
}
