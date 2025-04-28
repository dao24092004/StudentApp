package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.ClassGroupRequestDTO;
import com.studentApp.dto.response.ClassGroupResponseDTO;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Major;
import com.studentApp.entity.Semester;
import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.MajorRepository;
import com.studentApp.repository.SemesterRepository;

@Service
public class ClassGroupService {
	@Autowired
	private ClassGroupRepository classGroupRepository;
	@Autowired
	private MajorRepository majorRepository;
	@Autowired
	private SemesterRepository semesterRepository;

	@Transactional
	public ClassGroupResponseDTO createClassGroup(ClassGroupRequestDTO dto) {
		ClassGroup classGroup = new ClassGroup();
		classGroup.setGroupCode(dto.getGroupCode());
		classGroup.setGroupName(dto.getGroupName());
		Major major = majorRepository.findByMajorName(dto.getMajorName())
				.orElseThrow(() -> new RuntimeException("Major not found: " + dto.getMajorName()));
		classGroup.setMajor(major);
		classGroup.setShift(dto.getShift());
		Semester semester = semesterRepository.findByStartDate(dto.getSemesterStartDate()).orElseThrow(
				() -> new RuntimeException("Semester not found with start date: " + dto.getSemesterStartDate()));
		classGroup.setSemester(semester);
		classGroup = classGroupRepository.save(classGroup);
		return mapToClassGroupResponseDTO(classGroup);
	}

	public List<ClassGroupResponseDTO> getAllClassGroups() {
		return classGroupRepository.findAll().stream().map(this::mapToClassGroupResponseDTO)
				.collect(Collectors.toList());
	}

	private ClassGroupResponseDTO mapToClassGroupResponseDTO(ClassGroup classGroup) {
		ClassGroupResponseDTO dto = new ClassGroupResponseDTO();
		dto.setGroupCode(classGroup.getGroupCode());
		dto.setGroupName(classGroup.getGroupName());
		dto.setMajorName(classGroup.getMajor().getMajorName());
		dto.setShift(classGroup.getShift());
		dto.setSemesterStartDate(classGroup.getSemester().getStartDate());
		return dto;
	}
}
