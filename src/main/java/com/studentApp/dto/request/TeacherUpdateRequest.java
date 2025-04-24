package com.studentApp.dto.request;

import java.sql.Date;

import com.studentApp.entity.Teacher.Gender;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class TeacherUpdateRequest {

	private String teacherName;

	private Date teacherDateOfBirth;

	private Gender teacherGender;

	private String teacherAddress;

	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
	private String teacherPhoneNumber;

	private String teacherEmail;

	private Long deptId;
}