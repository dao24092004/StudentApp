package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClassRequestDTO {
	private String classCode;
	private String className;
	private String subjectName; // Thay vì subjectId
	private String teacherName; // Thay vì teacherId
	private String groupCode; // Thay vì classGroupId
	private LocalDate startDate;
	private LocalDate endDate;
	private String classroom;
	private String shift; // morning hoặc afternoon
	private Integer priority;
}