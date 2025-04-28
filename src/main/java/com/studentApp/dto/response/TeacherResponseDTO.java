package com.studentApp.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TeacherResponseDTO {
	private String teacherCode;
	private String teacherName;
	private LocalDate teacherDateOfBirth;
	private String teacherGender; // Male hoặc Female
	private String teacherAddress;
	private String teacherPhoneNumber;
	private String teacherEmail;
	private String deptName; // Thay vì deptId
}