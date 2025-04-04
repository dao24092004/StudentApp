package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PermissionResponse {
	private Long id;
	private String permissionName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}