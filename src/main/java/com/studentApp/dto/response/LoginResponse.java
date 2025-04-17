package com.studentApp.dto.response;

import com.studentApp.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class LoginResponse {
	private String accessToken;
	private String refreshToken;
	private User user;

}