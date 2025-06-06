package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class DepartmentResponseDTO {
	private String deptCode;
	private String deptName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
