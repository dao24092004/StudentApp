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

	@JsonProperty("user_email")
	private String userEmail;

	@JsonProperty("gender")
	private String gender;

	@JsonProperty("address")
	private String address;

	@JsonProperty("phone_number")
	private String phoneNumber;

	@JsonProperty("student_email")
	private String studentEmail;

	@JsonProperty("major_id")
	private String majorName; // Chuyển từ Long thành String

	@JsonProperty("class_group_id")
	private String classGroupName; // Chuyển từ Long thành String

	@JsonProperty("user_id")
	private String userName; // Chuyển từ Long thành String

	@JsonProperty("student_id")
    private Long studentId;
}