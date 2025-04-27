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

import com.studentApp.dto.request.ClassRequestDTO;
import com.studentApp.dto.response.ClassResponseDTO;
import com.studentApp.service.ClassService;

@RestController
@RequestMapping("/api/classes")
public class ClassController {
	@Autowired
	private ClassService classService;

	@PostMapping("/create/schedules")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<ClassResponseDTO> createClass(@RequestBody ClassRequestDTO dto) {
		return ResponseEntity.ok(classService.createClass(dto));
	}

	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
	public ResponseEntity<ClassResponseDTO> updateClass(@PathVariable Long id, @RequestBody ClassRequestDTO dto) {
		return ResponseEntity.ok(classService.updateClass(id, dto));
	}

	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('CLASS_DELETE')")
	public ResponseEntity<String> deleteClass(@PathVariable Long id) {
		classService.deleteClass(id);
		return ResponseEntity.ok("Class deleted successfully");
	}

	@GetMapping
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ClassResponseDTO>> getAllClasses() {
		return ResponseEntity.ok(classService.getAllClasses());
	}
}