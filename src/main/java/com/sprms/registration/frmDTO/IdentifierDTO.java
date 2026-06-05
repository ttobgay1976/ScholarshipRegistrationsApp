package com.sprms.registration.frmDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IdentifierDTO {

    @JsonProperty("schema_id")
    private String schemaId;

    @JsonProperty("cred_def_id")
    private String credDefId;
    
	public String getSchemaId() {
		return schemaId;
	}
	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}
	public String getCredDefId() {
		return credDefId;
	}
	public void setCredDefId(String credDefId) {
		this.credDefId = credDefId;
	}
    
    
}
