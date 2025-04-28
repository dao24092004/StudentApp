package com.studentApp.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClassResponseDTO {
	private String classCode;
	private String className;
	private String subjectName;
	private String teacherName;
	private String groupCode;
	private LocalDate startDate;
	private LocalDate endDate;
	private String classroom;
	private String shift;
	private Integer priority;
}