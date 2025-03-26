package com.studentApp.dto.response;

import lombok.Data;

@Data
public class LoginResponse {
	private String token;
	private UserResponse user;

	public LoginResponse() {
	}

	public LoginResponse(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
