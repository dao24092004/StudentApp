package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SubjectRequestDTO {
	private String subjectCode;
	private String subjectName;
	private Integer credits;
	private LocalDate semesterStartDate; // Thay vì semesterId
	private String deptName; // Thay vì deptId
	private String description;
}
