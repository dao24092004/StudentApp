package com.studentApp.dto.response;

import lombok.Data;

@Data
public class TeacherSubjectRegistrationResponse {
	private Long id;
	private Long teacherId;
	private Long subjectId;
	private Long semesterId;

}