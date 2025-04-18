package com.studentApp.controller;

import java.util.List;

import org.slf4j.LoggerFactory;
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

import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.request.StudentUpdateRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/users")
public class StudentController {

	@Autowired
	StudentService studentService;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(StudentController.class);

	@GetMapping("/student")
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ResponseEntity<List<StudentResponse>> getAllStudent() {
		log.info("Getting all students");
		List<StudentResponse> response = studentService.getAllStudent();
		log.info("Retrieved {} students", response.size());
		return ResponseEntity.ok(response);
	}

	@GetMapping("/student/{id}")
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ResponseEntity<StudentResponse> getIDStudent(@PathVariable long id) {
		log.info("Getting student with ID: {}", id);
		StudentResponse studentResponse = studentService.getIDStudent(id);
		log.info("Retrieved student: {}", studentResponse.getStudentCode());
		return ResponseEntity.ok(studentResponse);
	}

	@PostMapping("/student")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	@Operation(summary = "Create a new student")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student created successfully"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentCreationRequest request) {
		log.info("Creating student with email: {}", request.getEmailUser());
		StudentResponse student = studentService.createStudent(request);
		log.info("Created student: {}", student.getStudentCode());
		return ResponseEntity.ok(student);
	}

	@PutMapping("/student/{id}")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	@Operation(summary = "Update a student")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Student updated successfully"),
			@ApiResponse(responseCode = "400", description = "Invalid request"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id,
			@RequestBody StudentUpdateRequest request) {
		StudentResponse studentResponse = studentService.updateStudent(id, request);
		return ResponseEntity.ok(studentResponse);
	}

	@DeleteMapping("/student/delete/{id}")
	@PreAuthorize("hasAuthority('USER_DELETE')")
	@Operation(summary = "Delete a student", description = "Delete a student by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Student deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<String> deleteStudEntity9Entity(@PathVariable Long id) {
		studentService.deleteStudent(id);
		return ResponseEntity.ok("Student deleted successfully");
	}
}