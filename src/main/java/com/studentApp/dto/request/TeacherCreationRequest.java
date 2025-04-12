package com.studentApp.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherCreationRequest {
	private String teacherCode;
	private String teacherName;
	private String dateOfBirth;
}
