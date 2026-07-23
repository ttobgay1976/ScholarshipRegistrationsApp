package com.sprms.registration.frmbean;

import lombok.Data;

@Data
public class AuthenticationDTO {

    private String type;
    private String version;
    private AuthDataDTO data;
    
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public AuthDataDTO getData() {
		return data;
	}
	public void setData(AuthDataDTO data) {
		this.data = data;
	}
    
    
}
