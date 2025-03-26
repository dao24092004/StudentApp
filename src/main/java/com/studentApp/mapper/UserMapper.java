package com.studentApp.mapper;

import org.springframework.stereotype.Component;

import com.studentApp.Entity.User;
import com.studentApp.dto.response.UserResponse;

@Component
public class UserMapper {

	public UserResponse toUserResponse(User user) {
		UserResponse response = new UserResponse();
		response.setUsername(user.getUsername());
		response.setEmail(user.getEmail());
		response.setRoleName(user.getRole() != null ? user.getRole().getRoleName() : null);
		response.setAvatarUrl(user.getAvatarUrl());
		return response;
	}
}