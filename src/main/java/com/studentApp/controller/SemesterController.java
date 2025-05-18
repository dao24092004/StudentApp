package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.SemesterRequestDTO;
import com.studentApp.dto.response.SemesterResponseDTO;
import com.studentApp.service.SemesterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

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

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('USER_DELETE')")
	@Operation(summary = "Delete a student", description = "Delete a semester by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Semester deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Semester not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<String> deleteStudEntity9Entity(@PathVariable Long id) {
		semesterService.deleteSemesterById(id);
		return ResponseEntity.ok("Student deleted successfully");
	}
}
