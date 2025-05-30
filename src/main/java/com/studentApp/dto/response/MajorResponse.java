package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MajorResponse {
	private Long id;
	private String majorCode;
	private String majorName;
	private Long deptId;
	private String deptName;
	private String curriculumName; // Thêm trường này
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}