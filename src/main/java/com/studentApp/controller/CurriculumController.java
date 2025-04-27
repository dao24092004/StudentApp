package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.CurriculumRequestDTO;
import com.studentApp.dto.response.CurriculumResponseDTO;
import com.studentApp.service.CurriculumService;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {
	@Autowired
	private CurriculumService curriculumService;

	// POST: Tạo mới curriculum
	// Không có quyền cụ thể trong SecurityConfig, chỉ yêu cầu đăng nhập
	@PostMapping
	public ResponseEntity<CurriculumResponseDTO> createCurriculum(@RequestBody CurriculumRequestDTO dto) {
		return ResponseEntity.ok(curriculumService.createCurriculum(dto));
	}

	// GET: Lấy danh sách curriculum
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@GetMapping
	public ResponseEntity<List<CurriculumResponseDTO>> getAllCurriculums() {
		return ResponseEntity.ok(curriculumService.getAllCurriculums());
	}
}