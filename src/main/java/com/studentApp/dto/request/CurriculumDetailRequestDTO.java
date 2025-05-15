package com.studentApp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CurriculumDetailRequestDTO {
	@NotNull(message = "Curriculum ID is required")
	private Long curriculumId;

	@NotNull(message = "Subject ID is required")
	private Long subjectId;

	@NotNull(message = "Semester ID is required")
	private Long semesterId;

	private Boolean isMandatory;
}
