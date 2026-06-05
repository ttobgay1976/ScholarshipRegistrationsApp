package com.sprms.registration.frmDTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;



@Data
public class PresentationResultRequestDTO {

	@NotBlank
	private String type;

	@JsonProperty("verification_result")
	private String verificationResult;

	@JsonProperty("relationship_did")
	@NotBlank
	private String relationshipDid;

	@NotBlank
	private String thid;

	@JsonProperty("holder_did")
	@NotBlank
	private String holderDid;

	@Valid
	@JsonProperty("requested_presentation")
	private RequestedPresentationDTO requestedPresentation;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVerificationResult() {
		return verificationResult;
	}

	public void setVerificationResult(String verificationResult) {
		this.verificationResult = verificationResult;
	}

	public String getRelationshipDid() {
		return relationshipDid;
	}

	public void setRelationshipDid(String relationshipDid) {
		this.relationshipDid = relationshipDid;
	}

	public String getThid() {
		return thid;
	}

	public void setThid(String thid) {
		this.thid = thid;
	}

	public String getHolderDid() {
		return holderDid;
	}

	public void setHolderDid(String holderDid) {
		this.holderDid = holderDid;
	}

	public RequestedPresentationDTO getRequestedPresentation() {
		return requestedPresentation;
	}

	public void setRequestedPresentation(RequestedPresentationDTO requestedPresentation) {
		this.requestedPresentation = requestedPresentation;
	}

	
}
