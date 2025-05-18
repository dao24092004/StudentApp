package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleResponse {
	private Long id;
	private Long classId;
	private Long subjectId;
	private String dayOfWeek;
	private Integer slot;
	private Integer period;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private Long semesterId;
	private Integer week;
	private String status;
}