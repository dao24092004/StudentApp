package com.studentApp.dto.response;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.studentApp.entity.Teacher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonDeserialize(builder = TeacherResponse.TeacherResponseBuilder.class)
public class TeacherResponse {
	private Long id;
	private Long userId;
	private String userEmail;
	private String teacherName;
	private Date teacherDateOfBirth;
	private Teacher.Gender teacherGender;
	private String teacherAddress;
	private String teacherPhoneNumber;
	private String teacherCode;
	private String teacherEmail;

	@JsonPOJOBuilder(withPrefix = "")
	public static class TeacherResponseBuilder {
	}
}
