package com.studentApp.dto.request;

import lombok.Data;

@Data
public class AssignPermissionRequest {
	private String roleName;
	private String permissionName;
}