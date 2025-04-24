package com.studentApp.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class TeacherSubjectRegistrationRequest {
	private Long teacherId;
	private List<Long> subjectIds;
	private Long semesterId;
}