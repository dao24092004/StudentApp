package com.studentApp.dto.request;

import lombok.Data;

@Data
public class MajorRequestDTO {
	private String majorCode;
	private String majorName;
	private String deptName; // Thay vì deptId
	private String curriculumName; // Thay vì curriculumId
	private String description;
}