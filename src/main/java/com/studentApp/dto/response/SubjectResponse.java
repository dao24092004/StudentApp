package com.studentApp.dto.response;

import lombok.Data;

@Data
public class SubjectResponse {
	private Long id;
	private String subjectCode;
	private String subjectName;
	private Integer credits;
	private String description;
	private String semesterName; // Thay vì semesterId
	private String deptName; // Thay vì deptId
}