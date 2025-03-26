package com.studentApp.dto.request;

import lombok.Data;

@Data
public class MajorCreationRequest {
	private String majorCode;
	private String majorName;
	private String deptName;
	private String description;
}