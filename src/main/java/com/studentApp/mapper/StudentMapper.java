package com.studentApp.mapper;

import com.studentApp.dto.response.StudentResponse;
import com.studentApp.entity.Student;

public class StudentMapper {

	public static StudentResponse toStudentResponse(Student student) {
		if (student == null) {
			return null;
		}

		StudentResponse response = new StudentResponse();
		response.setStudentId(student.getId());
		response.setStudentCode(student.getStudentCode());
		response.setStudentName(student.getStudentName());
		response.setMajorName(student.getMajor() != null ? student.getMajor().getMajorName() : null);
		response.setUserEmail(student.getUser() != null ? student.getUser().getEmail() : null);
		response.setDateOfBirth(student.getDateOfBirth()); // Đảm bảo ánh xạ
		response.setGender(student.getGender() != null ? student.getGender().toString() : null);
		response.setAddress(student.getAddress());
		response.setPhoneNumber(student.getPhoneNumber());
		response.setStudentEmail(generateEmail(student.getStudentName(), student.getStudentCode())); // Thêm logic sinh
		response.setClassGroupName(student.getClassGroup() != null ? student.getClassGroup().getGroupName() : null);
	    response.setUserName(student.getUser() != null ? student.getUser().getUsername() : null);

		return response;
	}

	private static String generateEmail(String name, String studentCode) {
		if (name == null || studentCode == null) {
			return null;
		}
		String normalized = name.trim().toLowerCase().replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
				.replaceAll("[èéẹẻẽêềếệểễ]", "e").replaceAll("[ìíịỉĩ]", "i").replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
				.replaceAll("[ùúụủũưừứựửữ]", "u").replaceAll("[ỳýỵỷỹ]", "y").replaceAll("đ", "d")
				.replaceAll("[^a-z0-9]", "");
		return normalized + "_" + studentCode.toLowerCase() + "@university.edu.vn";
	}
}