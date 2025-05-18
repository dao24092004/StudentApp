package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CurriculumDetailResponseDTO {
	private Long id;
	private Long curriculumId;
	private String curriculumName; // Thêm trường tên chương trình khung
	private Long subjectId;
	private String subjectName; // Thêm trường tên môn học
	private Long semesterId;
	private String semesterName; // Thêm trường tên kỳ học
	private Integer Credits;
	private Integer TheoryPeriods;
	private Integer PracticalPeriods;
	private Boolean isMandatory;
	private LocalDateTime createdAt;

}
