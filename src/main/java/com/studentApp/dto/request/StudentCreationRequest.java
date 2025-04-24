package com.studentApp.dto.request;

import java.sql.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentCreationRequest {

	@NotBlank(message = "Student name is required")
	private String student_name;

	@NotNull(message = "Date of birth is required")
	private Date date_of_birth;

	@NotBlank(message = "Gender is required")
	@Pattern(regexp = "Male|Female", message = "Gender must be one of: Male, Female")
	private String gender;

	private String address;

	private String phone_number;

	@NotNull(message = "Major ID is required")
	private Long major_id;

	@NotNull(message = "Class group ID is required")
	private Long class_group_id;

	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String emailUser;
}