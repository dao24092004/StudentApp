package com.studentApp.dto.request;

import lombok.Data;

@Data
public class DepartmentCreationRequest {
	private String deptCode;
	private String deptName;
	private String description;
}