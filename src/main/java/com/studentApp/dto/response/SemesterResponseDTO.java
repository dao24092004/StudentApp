package com.studentApp.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class SemesterResponseDTO {
	private LocalDate startDate;
	private LocalDate endDate;
}