package com.studentApp.dto.request;

import lombok.Data;

@Data
public class RevokePermissionRequest {
	private String roleName;
	private String permissionName;
}