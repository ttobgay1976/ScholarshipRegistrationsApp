package com.sprms.registration.frmbean;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RequestedPresentationDTO {

    @JsonProperty("unrevealed_attrs")
    private Map<String, Object> unrevealedAttrs;

    private Map<String, Object> predicates;

    @JsonProperty("self_attested_attrs")
    private Map<String, Object> selfAttestedAttrs;

    private List<IdentifierDTO> identifiers;

    @JsonProperty("revealed_attrs")
    private Map<String, Object> revealedAttrs;  // 🔥 CHANGE HERE (IMPORTANT)
    

	public Map<String, Object> getUnrevealedAttrs() {
		return unrevealedAttrs;
	}

	public void setUnrevealedAttrs(Map<String, Object> unrevealedAttrs) {
		this.unrevealedAttrs = unrevealedAttrs;
	}

	public Map<String, Object> getPredicates() {
		return predicates;
	}

	public void setPredicates(Map<String, Object> predicates) {
		this.predicates = predicates;
	}

	public Map<String, Object> getSelfAttestedAttrs() {
		return selfAttestedAttrs;
	}

	public void setSelfAttestedAttrs(Map<String, Object> selfAttestedAttrs) {
		this.selfAttestedAttrs = selfAttestedAttrs;
	}

	public List<IdentifierDTO> getIdentifiers() {
		return identifiers;
	}

	public void setIdentifiers(List<IdentifierDTO> identifiers) {
		this.identifiers = identifiers;
	}

	public Map<String, Object> getRevealedAttrs() {
		return revealedAttrs;
	}

	public void setRevealedAttrs(Map<String, Object> revealedAttrs) {
		this.revealedAttrs = revealedAttrs;
	}


    
    
    
}
