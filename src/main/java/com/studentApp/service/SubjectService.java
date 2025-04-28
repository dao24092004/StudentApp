package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.SubjectRequestDTO;
import com.studentApp.dto.response.SubjectResponseDTO;
import com.studentApp.entity.Department;
import com.studentApp.entity.Semester;
import com.studentApp.entity.Subject;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.SubjectRepository;

@Service
public class SubjectService {
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private SemesterRepository semesterRepository;

	@Transactional
	public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {
		Subject subject = new Subject();
		subject.setSubjectCode(dto.getSubjectCode());
		subject.setSubjectName(dto.getSubjectName());
		subject.setCredits(dto.getCredits());
		Semester semester = semesterRepository.findByStartDate(dto.getSemesterStartDate()).orElseThrow(
				() -> new RuntimeException("Semester not found with start date: " + dto.getSemesterStartDate()));
		subject.setSemester(semester);
		Department department = departmentRepository.findByDeptName(dto.getDeptName())
				.orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDeptName()));
		subject.setDeptId(department.getId());
		subject.setDescription(dto.getDescription());
		subject = subjectRepository.save(subject);
		return mapToSubjectResponseDTO(subject);
	}

	public List<SubjectResponseDTO> getAllSubjects() {
		return subjectRepository.findAll().stream().map(this::mapToSubjectResponseDTO).collect(Collectors.toList());
	}

	private SubjectResponseDTO mapToSubjectResponseDTO(Subject subject) {
		SubjectResponseDTO dto = new SubjectResponseDTO();
		dto.setSubjectCode(subject.getSubjectCode());
		dto.setSubjectName(subject.getSubjectName());
		dto.setCredits(subject.getCredits());
		dto.setSemesterStartDate(subject.getSemester().getStartDate());
		Department department = departmentRepository.findById(subject.getDeptId())
				.orElseThrow(() -> new RuntimeException("Department not found: " + subject.getDeptId()));
		dto.setDeptName(department.getDeptName());
		dto.setDescription(subject.getDescription());
		return dto;
	}
}
