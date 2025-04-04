package com.studentApp.dto.request;

import lombok.Data;

@Data
public class PermissionCreationRequest {
	private String permissionName;
	private String description;
}