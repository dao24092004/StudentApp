package com.studentApp.mapper;

import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.entity.Teacher;

public class TeacherMapper {
	public static TeacherResponse toTeacherResponse(Teacher teacher) {
		TeacherResponse response = new TeacherResponse();
		response.setId(teacher.getId());
		response.setUserId(teacher.getUser().getId());
		response.setUserEmail(teacher.getTeacherEmail());
		response.setTeacherName(teacher.getTeacherName());
		response.setTeacherDateOfBirth(teacher.getTeacherDateOfBirth());
		response.setTeacherCode(teacher.getTeacherCode());
		response.setTeacherGender(teacher.getTeacherGender());
		response.setTeacherAddress(teacher.getTeacherAddress());
		response.setTeacherEmail(teacher.getTeacherEmail());
		response.setTeacherPhoneNumber(teacher.getTeacherPhoneNumber());

		return response;
	}

	public static TeacherResponse toTeacherResponse(TeacherCreationRequest teacherCreationRequest) {
		TeacherResponse response = new TeacherResponse();
		response.setUserEmail(teacherCreationRequest.getUserEmail());
		response.setTeacherName(teacherCreationRequest.getTeacherName());
		response.setTeacherDateOfBirth(teacherCreationRequest.getTeacherDateOfBirth());
		response.setTeacherGender(teacherCreationRequest.getTeacherGender());
		response.setTeacherAddress(teacherCreationRequest.getTeacherAddress());
		response.setTeacherPhoneNumber(teacherCreationRequest.getTeacherPhoneNumber());

		return response;
	}
}