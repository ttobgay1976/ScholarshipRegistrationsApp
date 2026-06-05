package com.sprms.registration.ndievent;

import com.sprms.registration.frmDTO.PresentationResultRequestDTO;

public class NdiVerifiedEvent {

	private final PresentationResultRequestDTO payload;
	private final String thid;

	public NdiVerifiedEvent(PresentationResultRequestDTO payload, String thid) {
		this.payload = payload;
		this.thid = thid;
	}

	public PresentationResultRequestDTO getPayload() {
		return payload;
	}

	public String getThid() {
		return thid;
	}
}
