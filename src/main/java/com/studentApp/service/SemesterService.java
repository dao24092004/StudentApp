package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.SemesterRequestDTO;
import com.studentApp.dto.response.SemesterResponseDTO;
import com.studentApp.entity.Semester;
import com.studentApp.repository.SemesterRepository;

@Service
public class SemesterService {
	@Autowired
	private SemesterRepository semesterRepository;

	@Transactional
	public SemesterResponseDTO createSemester(SemesterRequestDTO dto) {
		Semester semester = new Semester();
		semester.setStartDate(dto.getStartDate());
		semester.setEndDate(dto.getEndDate());
		semester = semesterRepository.save(semester);
		return mapToSemesterResponseDTO(semester);
	}

	public List<SemesterResponseDTO> getAllSemesters() {
		return semesterRepository.findAll().stream().map(this::mapToSemesterResponseDTO).collect(Collectors.toList());
	}

	private SemesterResponseDTO mapToSemesterResponseDTO(Semester semester) {
		SemesterResponseDTO dto = new SemesterResponseDTO();
		dto.setStartDate(semester.getStartDate());
		dto.setEndDate(semester.getEndDate());
		return dto;
	}
}