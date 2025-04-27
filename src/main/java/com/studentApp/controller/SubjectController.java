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

import com.studentApp.dto.request.SubjectRequestDTO;
import com.studentApp.dto.response.SubjectResponseDTO;
import com.studentApp.service.SubjectService;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {
	@Autowired
	private SubjectService subjectService;

	// POST: Tạo mới subject
	// Yêu cầu quyền SUBJECT_CREATE
	@PostMapping("/create")
	@PreAuthorize("hasAuthority('SUBJECT_CREATE')")
	public ResponseEntity<SubjectResponseDTO> createSubject(@RequestBody SubjectRequestDTO dto) {
		return ResponseEntity.ok(subjectService.createSubject(dto));
	}

	// GET: Lấy danh sách subject
	// Yêu cầu quyền SUBJECT_VIEW
	@GetMapping
	@PreAuthorize("hasAuthority('SUBJECT_VIEW')")
	public ResponseEntity<List<SubjectResponseDTO>> getAllSubjects() {
		return ResponseEntity.ok(subjectService.getAllSubjects());
	}
}
