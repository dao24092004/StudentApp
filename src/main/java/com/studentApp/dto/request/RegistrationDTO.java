package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class RegistrationDTO {
	private Long id;
	private Long classId;
	private Long studentId;
	private Long semesterId;
	private LocalDate registrationDate;
	private String status;
}
