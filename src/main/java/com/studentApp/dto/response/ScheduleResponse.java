package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ScheduleResponse {
	private Long id;
	private String classCode; // Mã lớp thay vì classId
	private String className; // Tên lớp
	private String subjectCode; // Mã môn học thay vì subjectId
	private String subjectName; // Tên môn học
	private String dayOfWeek;
	private Integer slot;
	private Integer period;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private String teacherName; // Tên giáo viên
	private String studentName; // Tên sinh viên (nếu có)
}