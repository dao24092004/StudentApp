package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.TimeWindowRequestDTO;
import com.studentApp.dto.response.TimeWindowResponseDTO;
import com.studentApp.entity.Class;
import com.studentApp.entity.TimeWindow;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.TimeWindowRepository;

@Service
public class TimeWindowService {
	@Autowired
	private TimeWindowRepository timeWindowRepository;
	@Autowired
	private ClassRepository classRepository;

	@Transactional
	public TimeWindowResponseDTO createTimeWindow(TimeWindowRequestDTO dto) {
		TimeWindow timeWindow = new TimeWindow();
		Class clazz = classRepository.findByClassCode(dto.getClassCode())
				.orElseThrow(() -> new RuntimeException("Class not found: " + dto.getClassCode()));
		timeWindow.setIdClass(clazz.getId());
		timeWindow.setDayOfWeek(dto.getDayOfWeek());
		timeWindow.setSlot(dto.getSlot());
		timeWindow.setCreatedDate(dto.getCreatedDate());
		timeWindow.setClazz(clazz);
		timeWindow = timeWindowRepository.save(timeWindow);
		return mapToTimeWindowResponseDTO(timeWindow);
	}

	public List<TimeWindowResponseDTO> getAllTimeWindows() {
		return timeWindowRepository.findAll().stream().map(this::mapToTimeWindowResponseDTO)
				.collect(Collectors.toList());
	}

	private TimeWindowResponseDTO mapToTimeWindowResponseDTO(TimeWindow timeWindow) {
		TimeWindowResponseDTO dto = new TimeWindowResponseDTO();
		dto.setClassCode(timeWindow.getClazz().getClassCode());
		dto.setDayOfWeek(timeWindow.getDayOfWeek());
		dto.setSlot(timeWindow.getSlot());
		dto.setCreatedDate(timeWindow.getCreatedDate());
		return dto;
	}
}