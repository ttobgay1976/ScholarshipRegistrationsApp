package com.sprms.registration.frmDTO;

import lombok.Data;

@Data
public class AuthDataDTO {

	 private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	 
	 
}
