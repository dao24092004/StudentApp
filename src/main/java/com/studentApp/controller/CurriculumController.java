package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.CurriculumDetailRequestDTO;
import com.studentApp.dto.request.CurriculumRequestDTO;
import com.studentApp.dto.response.CurriculumDetailResponseDTO;
import com.studentApp.dto.response.CurriculumResponseDTO;
import com.studentApp.service.CurriculumService;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

	@Autowired
	private CurriculumService curriculumService;

	// POST: Tạo mới chương trình khung
	@PostMapping
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<CurriculumResponseDTO> createCurriculum(@RequestBody CurriculumRequestDTO dto) {
		return ResponseEntity.ok(curriculumService.createCurriculum(dto));
	}

	// GET: Lấy danh sách tất cả chương trình khung
	@GetMapping
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<CurriculumResponseDTO>> getAllCurriculums() {
		return ResponseEntity.ok(curriculumService.getAllCurriculums());
	}

	// PUT: Cập nhật chương trình khung
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
	public ResponseEntity<CurriculumResponseDTO> updateCurriculum(@PathVariable Long id,
			@RequestBody CurriculumRequestDTO dto) {
		return ResponseEntity.ok(curriculumService.updateCurriculum(id, dto));
	}

	// DELETE: Xóa chương trình khung
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('CLASS_DELETE')")
	public ResponseEntity<Void> deleteCurriculum(@PathVariable Long id) {
		curriculumService.deleteCurriculum(id);
		return ResponseEntity.noContent().build();
	}

	// POST: Tạo mới chi tiết chương trình khung
	@PostMapping("/details")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<CurriculumDetailResponseDTO> createCurriculumDetail(
			@RequestBody CurriculumDetailRequestDTO dto) {
		return ResponseEntity.ok(curriculumService.createCurriculumDetail(dto));
	}

	// GET: Lấy danh sách chi tiết chương trình khung theo curriculumId
	@GetMapping("/details/{curriculumId}")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<CurriculumDetailResponseDTO>> getCurriculumDetails(@PathVariable Long curriculumId) {
		return ResponseEntity.ok(curriculumService.getCurriculumDetails(curriculumId));
	}

	// GET: Lấy danh sách chi tiết chương trình khung theo studentId
	@GetMapping("/student/{studentId}")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<CurriculumDetailResponseDTO>> getCurriculumDetailsByStudentId(
			@PathVariable Long studentId) {
		return ResponseEntity.ok(curriculumService.getCurriculumDetailsByStudentId(studentId));
	}

	// PUT: Cập nhật chi tiết chương trình khung
	@PutMapping("/details/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
	public ResponseEntity<CurriculumDetailResponseDTO> updateCurriculumDetail(@PathVariable Long id,
			@RequestBody CurriculumDetailRequestDTO dto) {
		return ResponseEntity.ok(curriculumService.updateCurriculumDetail(id, dto));
	}

	// DELETE: Xóa chi tiết chương trình khung
	@DeleteMapping("/details/{id}")
	@PreAuthorize("hasAuthority('CLASS_DELETE')")
	public ResponseEntity<Void> deleteCurriculumDetail(@PathVariable Long id) {
		curriculumService.deleteCurriculumDetail(id);
		return ResponseEntity.noContent().build();
	}
}