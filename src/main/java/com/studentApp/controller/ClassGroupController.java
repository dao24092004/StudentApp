package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.ClassGroupRequestDTO;
import com.studentApp.dto.response.ClassGroupResponseDTO;
import com.studentApp.service.ClassGroupService;

@RestController
@RequestMapping("/api/classgroup")
public class ClassGroupController {
	@Autowired
	private ClassGroupService classGroupService;

	// POST: Tạo mới classgroup
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@PostMapping
	public ResponseEntity<ClassGroupResponseDTO> createClassGroup(@RequestBody ClassGroupRequestDTO dto) {
		return ResponseEntity.ok(classGroupService.createClassGroup(dto));
	}

	// GET: Lấy danh sách classgroup
	// Không có quyền cụ thể, chỉ yêu cầu đăng nhập
	@GetMapping
	public ResponseEntity<List<ClassGroupResponseDTO>> getAllClassGroups() {
		return ResponseEntity.ok(classGroupService.getAllClassGroups());
	}
}