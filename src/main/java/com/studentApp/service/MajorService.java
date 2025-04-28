package com.studentApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.MajorRequestDTO;
import com.studentApp.dto.response.MajorResponseDTO;
import com.studentApp.entity.Curriculum;
import com.studentApp.entity.Department;
import com.studentApp.entity.Major;
import com.studentApp.repository.CurriculumRepository;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.MajorRepository;

@Service
public class MajorService {
	@Autowired
	private MajorRepository majorRepository;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private CurriculumRepository curriculumRepository;

	@Transactional
	public MajorResponseDTO createMajor(MajorRequestDTO dto) {
		Major major = new Major();
		major.setMajorCode(dto.getMajorCode());
		major.setMajorName(dto.getMajorName());
		Department department = departmentRepository.findByDeptName(dto.getDeptName())
				.orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDeptName()));
		major.setDeptId(department.getId());
		Curriculum curriculum = curriculumRepository.findByCurriculumName(dto.getCurriculumName())
				.orElseThrow(() -> new RuntimeException("Curriculum not found: " + dto.getCurriculumName()));
		major.setCurriculum(curriculum);
		major.setDescription(dto.getDescription());
		major.setCreatedAt(LocalDateTime.now());
		major.setUpdatedAt(LocalDateTime.now());
		major = majorRepository.save(major);
		return mapToMajorResponseDTO(major);
	}

	public List<MajorResponseDTO> getAllMajors() {
		return majorRepository.findAll().stream().map(this::mapToMajorResponseDTO).collect(Collectors.toList());
	}

	private MajorResponseDTO mapToMajorResponseDTO(Major major) {
		MajorResponseDTO dto = new MajorResponseDTO();
		dto.setMajorCode(major.getMajorCode());
		dto.setMajorName(major.getMajorName());
		Department department = departmentRepository.findById(major.getDeptId())
				.orElseThrow(() -> new RuntimeException("Department not found: " + major.getDeptId()));
		dto.setDeptName(department.getDeptName());
		dto.setCurriculumName(major.getCurriculum().getCurriculumName());
		dto.setDescription(major.getDescription());
		dto.setCreatedAt(major.getCreatedAt());
		dto.setUpdatedAt(major.getUpdatedAt());
		return dto;
	}
}