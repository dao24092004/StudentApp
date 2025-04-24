package com.studentApp.dto.request;

import java.sql.Date;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StudentUpdateRequest {

	private String studentName;

	private Date dateOfBirth;

	@Pattern(regexp = "Male|Female", message = "Gender must be one of: Male, Female")
	private String gender;

	private String address;

	private String phoneNumber;
}