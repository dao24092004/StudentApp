package com.studentApp.dto.response;

import com.studentApp.entity.User;

import lombok.Data;

@Data
public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private User user;

	public LoginResponse(String accessToken, String refreshToken, User user) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.user = user;
	}
}