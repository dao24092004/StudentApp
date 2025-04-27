package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.MajorRequestDTO;
import com.studentApp.dto.response.MajorResponseDTO;
import com.studentApp.service.MajorService;

@RestController
@RequestMapping("/api/major")
public class MajorController {
	@Autowired
	private MajorService majorService;

	// POST: Tạo mới major
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@PostMapping
	public ResponseEntity<MajorResponseDTO> createMajor(@RequestBody MajorRequestDTO dto) {
		return ResponseEntity.ok(majorService.createMajor(dto));
	}

	// GET: Lấy danh sách major
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@GetMapping
	public ResponseEntity<List<MajorResponseDTO>> getAllMajors() {
		return ResponseEntity.ok(majorService.getAllMajors());
	}
}