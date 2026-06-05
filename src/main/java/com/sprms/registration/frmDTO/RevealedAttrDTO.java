package com.sprms.registration.frmDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class RevealedAttrDTO {

    private String value;

    @JsonProperty("identifier_index")
    private Integer identifierIndex;
    
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Integer getIdentifierIndex() {
		return identifierIndex;
	}
	public void setIdentifierIndex(Integer identifierIndex) {
		this.identifierIndex = identifierIndex;
	}
    
    
}
