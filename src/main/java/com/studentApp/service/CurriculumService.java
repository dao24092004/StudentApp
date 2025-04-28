package com.studentApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.CurriculumRequestDTO;
import com.studentApp.dto.response.CurriculumResponseDTO;
import com.studentApp.entity.Curriculum;
import com.studentApp.repository.CurriculumRepository;

@Service
public class CurriculumService {
	@Autowired
	private CurriculumRepository curriculumRepository;

	@Transactional
	public CurriculumResponseDTO createCurriculum(CurriculumRequestDTO dto) {
		Curriculum curriculum = new Curriculum();
		curriculum.setCurriculumCode(dto.getCurriculumCode());
		curriculum.setCurriculumName(dto.getCurriculumName());
		curriculum.setDescription(dto.getDescription());
		curriculum.setCreatedAt(LocalDateTime.now());
		curriculum.setUpdatedAt(LocalDateTime.now());
		curriculum = curriculumRepository.save(curriculum);
		return mapToCurriculumResponseDTO(curriculum);
	}

	public List<CurriculumResponseDTO> getAllCurriculums() {
		return curriculumRepository.findAll().stream().map(this::mapToCurriculumResponseDTO)
				.collect(Collectors.toList());
	}

	private CurriculumResponseDTO mapToCurriculumResponseDTO(Curriculum curriculum) {
		CurriculumResponseDTO dto = new CurriculumResponseDTO();
		dto.setCurriculumCode(curriculum.getCurriculumCode());
		dto.setCurriculumName(curriculum.getCurriculumName());
		dto.setDescription(curriculum.getDescription());
		dto.setCreatedAt(curriculum.getCreatedAt());
		dto.setUpdatedAt(curriculum.getUpdatedAt());
		return dto;
	}
}