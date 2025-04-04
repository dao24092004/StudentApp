package com.studentApp.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
	private String username;
	private String password;
	private String email;
	private Long roleId;
}