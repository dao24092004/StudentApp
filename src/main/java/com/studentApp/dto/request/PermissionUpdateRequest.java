package com.studentApp.dto.request;

import lombok.Data;

@Data
public class PermissionUpdateRequest {
	private String permissionName;
	private String description;
}