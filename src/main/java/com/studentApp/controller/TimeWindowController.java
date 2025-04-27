package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.TimeWindowRequestDTO;
import com.studentApp.dto.response.TimeWindowResponseDTO;
import com.studentApp.service.TimeWindowService;

@RestController
@RequestMapping("/api/timewindow")
public class TimeWindowController {
	@Autowired
	private TimeWindowService timeWindowService;

	// POST: Tạo mới timewindow
	// Không có quyền cụ thể, nhưng giả định cần quyền SCHEDULE_CREATE (dựa trên
	// /api/classes/schedules/generate trong SecurityConfig)
	@PostMapping
	@PreAuthorize("hasAuthority('SCHEDULE_CREATE')")
	public ResponseEntity<TimeWindowResponseDTO> createTimeWindow(@RequestBody TimeWindowRequestDTO dto) {
		return ResponseEntity.ok(timeWindowService.createTimeWindow(dto));
	}

	// GET: Lấy danh sách timewindow
	// Yêu cầu quyền CLASS_VIEW (dựa trên /api/classes/schedules/** trong
	// SecurityConfig)
	@GetMapping
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<TimeWindowResponseDTO>> getAllTimeWindows() {
		return ResponseEntity.ok(timeWindowService.getAllTimeWindows());
	}
}