package com.studentApp.dto.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {

	@JsonProperty("student_code")
	private String studentCode;

	@JsonProperty("student_name")
	private String studentName;

	@JsonProperty("date_of_birth")
	private Date dateOfBirth;

	private String userEmail;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("address")
	private String address;

	@JsonProperty("phone_number")
	private String phoneNumber;

	private String studentEmail;
}