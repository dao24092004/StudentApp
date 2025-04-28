package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SemesterRequestDTO {
	private LocalDate startDate;
	private LocalDate endDate;
}