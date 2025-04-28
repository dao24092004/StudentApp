package com.studentApp.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TimeWindowRequestDTO {
	private String classCode; // Thay vì idClass
	private String dayOfWeek; // Mon, Tue, ...
	private Integer slot; // 1, 2, 3, ...
	private LocalDate createdDate;
}