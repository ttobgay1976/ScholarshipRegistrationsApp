package com.sprms.registration.frmbean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProofRequestPayloadDTO {

	 private String purpose;
	    private String proofName;
	    private List<ProofAttribute> proofAttributes;

	    public ProofRequestPayloadDTO(String purpose, String proofName, List<ProofAttribute> proofAttributes) {
	        this.purpose = purpose;
	        this.proofName = proofName;
	        this.proofAttributes = proofAttributes;
	    }

	    public String getPurpose() {
	        return purpose;
	    }

	    public String getProofName() {
	        return proofName;
	    }

	    public List<ProofAttribute> getProofAttributes() {
	        return proofAttributes;
	    }

	    // ================= INNER CLASS =================
	    public static class ProofAttribute {

	        private String name;
	        private List<Restriction> restrictions;

	        public ProofAttribute(String name, List<Restriction> restrictions) {
	            this.name = name;
	            this.restrictions = restrictions;
	        }

	        public String getName() {
	            return name;
	        }

	        public List<Restriction> getRestrictions() {
	            return restrictions;
	        }
	    }

	    // ================= INNER CLASS =================
	    public static class Restriction {

	    	@JsonProperty("schema_name")
	        private String schemaName;

	        public Restriction(String schemaName) {
	            this.schemaName = schemaName;
	        }

	        public String getSchemaName() {
	            return schemaName;
	        }

	        public void setSchemaName(String schemaName) {
	            this.schemaName = schemaName;
	        }
	    }
}