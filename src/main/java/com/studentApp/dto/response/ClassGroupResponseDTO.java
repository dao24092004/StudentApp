package com.studentApp.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class ClassGroupResponseDTO {
	private Long id;
	private String groupCode;
	private String groupName;
	private String majorName; // Thay vì majorId
	private String shift; // morning hoặc afternoon
	private LocalDate semesterStartDate; // Thay vì semesterId
}
