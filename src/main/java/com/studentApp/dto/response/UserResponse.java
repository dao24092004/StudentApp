package com.studentApp.dto.response;

import lombok.Data;

@Data
public class UserResponse {
	private String username;
	private String email;
	private String roleName;
	private String avatarUrl;
}
