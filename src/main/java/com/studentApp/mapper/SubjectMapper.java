package com.studentApp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.entity.Department;
import com.studentApp.entity.Subject;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.SemesterRepository;

@Component
public class SubjectMapper {

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public SubjectResponse toDTO(Subject subject) {
		SubjectResponse dto = new SubjectResponse();
		dto.setId(subject.getId());
		dto.setSubjectCode(subject.getSubjectCode());
		dto.setSubjectName(subject.getSubjectName());
		dto.setCredits(subject.getCredits());
		dto.setDescription(subject.getDescription());

		if (subject.getSemester() != null) {
			dto.setSemesterName(subject.getSemester().getStartDate() + " - " + subject.getSemester().getEndDate());
		}

		Department department = departmentRepository.findById(subject.getId())
				.orElseThrow(() -> new RuntimeException("Department not found with id: " + subject.getDeptId()));
		dto.setDeptName(department.getDeptName());

		return dto;
	}
}