package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CurriculumResponseDTO {
	private String curriculumCode;
	private String curriculumName;
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
