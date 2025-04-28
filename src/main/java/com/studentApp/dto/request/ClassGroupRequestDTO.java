package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClassGroupRequestDTO {
	private String groupCode;
	private String groupName;
	private String majorName; // Thay vì majorId
	private String shift; // morning hoặc afternoon
	private LocalDate semesterStartDate; // Thay vì semesterId
}