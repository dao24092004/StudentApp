package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.SemesterRequestDTO;
import com.studentApp.dto.response.SemesterResponseDTO;
import com.studentApp.service.SemesterService;

@RestController
@RequestMapping("/api/semester")
public class SemesterController {
	@Autowired
	private SemesterService semesterService;

	// POST: Tạo mới semester
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@PostMapping
	public ResponseEntity<SemesterResponseDTO> createSemester(@RequestBody SemesterRequestDTO dto) {
		return ResponseEntity.ok(semesterService.createSemester(dto));
	}

	// GET: Lấy danh sách semester
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@GetMapping
	public ResponseEntity<List<SemesterResponseDTO>> getAllSemesters() {
		return ResponseEntity.ok(semesterService.getAllSemesters());
	}
}