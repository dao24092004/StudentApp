package com.studentApp.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.service.RegistrationService;

// Controller để xử lý các API liên quan đến đăng ký học phần
@RestController
@RequestMapping("/api/classes/registration")
public class RegistrationController {
	private final RegistrationService registrationService;

	// Constructor
	public RegistrationController(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	// API để đăng ký học phần (chỉ học sinh)
	@PostMapping("/register")
	@PreAuthorize("hasAuthority('STUDENT_REGISTER')")
	public ResponseEntity<?> registerClass(@RequestBody RegistrationRequest request) {
		try {
			registrationService.registerClass(request.getStudentId(), request.getClassId(), request.getSemesterId());
			return ResponseEntity.ok(Map.of("status", "success"));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("status", "error", "message", e.getMessage()));
		}
	}
}

// Class để nhận request body cho API /register
class RegistrationRequest {
	private Long studentId;
	private Long classId;
	private Long semesterId;

	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Long semesterId) {
		this.semesterId = semesterId;
	}
}