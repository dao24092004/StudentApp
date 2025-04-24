package com.studentApp.mapper;

import com.studentApp.dto.response.StudentResponse;
import com.studentApp.entity.Student;

public class StudentMapper {

	public static StudentResponse toStudentResponse(Student student) {
		return StudentResponse.builder().studentCode(student.getStudentCode()).studentName(student.getStudentName())
				.dateOfBirth(student.getDateOfBirth())
				.userEmail(student.getUser() != null ? student.getUser().getEmail() : null)
				.gender(student.getGender() != null ? student.getGender().name() : null).address(student.getAddress())
				.phoneNumber(student.getPhoneNumber())
				.studentEmail(generateEmail(student.getStudentName(), student.getStudentCode()))
				.majorId(student.getMajor() != null ? String.valueOf(student.getMajor().getId()) : null)
				.classGroupId(student.getClassGroup() != null ? String.valueOf(student.getClassGroup().getId()) : null)
				.userId(student.getUser() != null ? String.valueOf(student.getUser().getId()) : null).build();
	}

	private static String generateEmail(String name, String studentCode) {
		String normalized = name.trim().toLowerCase().replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
				.replaceAll("[èéẹẻẽêềếệểễ]", "e").replaceAll("[ìíịỉĩ]", "i").replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
				.replaceAll("[ùúụủũưừứựửữ]", "u").replaceAll("[ỳýỵỷỹ]", "y").replaceAll("đ", "d")
				.replaceAll("[^a-z0-9]", "");
		return normalized + "_" + studentCode.toLowerCase() + "@university.edu.vn";
	}
}