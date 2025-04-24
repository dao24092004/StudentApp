package com.studentApp.dto.request;

import java.sql.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.studentApp.entity.Teacher.Gender;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonDeserialize(builder = TeacherCreationRequest.TeacherCreationRequestBuilder.class)
public class TeacherCreationRequest {

	private String userEmail;

	private String teacherName;

	private Date teacherDateOfBirth;

	private Gender teacherGender;

	private String teacherAddress;

	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
	private String teacherPhoneNumber;

	@NotNull(message = "Department ID is required")
	private Long deptId;

	@JsonPOJOBuilder(withPrefix = "")
	public static class TeacherCreationRequestBuilder {
	}
}