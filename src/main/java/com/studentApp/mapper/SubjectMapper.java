package com.studentApp.mapper;

import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.entity.Subject;

public class SubjectMapper {

	public static SubjectResponse toDTO(Subject subject) {
		SubjectResponse dto = new SubjectResponse();
		dto.setId(subject.getId());
		dto.setSubjectCode(subject.getSubjectCode());
		dto.setSubjectName(subject.getSubjectName());
		dto.setCredits(subject.getCredits());
		dto.setDescription(subject.getDescription());
		dto.setSemesterId(subject.getSemesterId());
		dto.setDeptId(subject.getDeptId());
		return dto;
	}
}