package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MajorResponseDTO {
	private Long id;
	private String majorCode;
	private String majorName;
	private String deptName; // Thay vì deptId
	private String curriculumName; // Thay vì curriculumId
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}