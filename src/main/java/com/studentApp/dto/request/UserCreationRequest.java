package com.studentApp.dto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserCreationRequest {
	private int id;
	private String email;
	private String username;
	private int roleId;
	private String userType;
	private Date createdAt;
	private Date updatedAt;
	private String password;

}
