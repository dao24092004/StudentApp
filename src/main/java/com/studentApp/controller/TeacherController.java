package com.studentApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.request.TeacherUpdateRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.service.TeacherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class TeacherController {

	private static final Logger log = LoggerFactory.getLogger(TeacherController.class);

	@Autowired
	private TeacherService teacherService;

	@GetMapping("/teacher")
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ResponseEntity<List<TeacherResponse>> getAllTeacher() {
		return ResponseEntity.ok(teacherService.getAllTeacher());
	}

	@GetMapping("/teacher/{id}")
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ResponseEntity<TeacherResponse> getByIdTeacher(@PathVariable long id) {
		return ResponseEntity.ok(teacherService.getByIdTeacher(id));
	}

	@PostMapping("/create/teacher")
	@PreAuthorize("hasAuthority('USER_CREATE')")
	@Operation(summary = "Create a new teacher", description = "Create a new teacher for the system")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Teacher created successfully"),
			@ApiResponse(responseCode = "404", description = "Teacher not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<?> createTeacher(@Valid @RequestBody TeacherCreationRequest request) {
		try {
			log.info("Creating teacher with email: {}", request.getUserEmail());
			Object response = teacherService.createTeacher(request);
			log.info("Teacher created successfully: {}", request.getTeacherName());
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			log.error("Error creating teacher", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error creating teacher: " + e.getMessage());
		}
	}

	@PutMapping("/update/teacher/{id}")
	@PreAuthorize("hasAuthority('USER_UPDATE')")
	@Operation(summary = "Update a teacher", description = "Update an existing teacher by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Teacher updated successfully"),
			@ApiResponse(responseCode = "404", description = "Teacher not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable long id,
			@RequestBody @Valid TeacherUpdateRequest request) {
		TeacherResponse response = teacherService.updateTeacher(id, request);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/delete/teacher/{id}")
	@PreAuthorize("hasAuthority('USER_DELETE')")
	@Operation(summary = "Delete a teacher", description = "Delete an existing teacher by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Teacher delete successfully"),
			@ApiResponse(responseCode = "404", description = "Teacher not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<String> deleteTeacher(@PathVariable long id) {
		teacherService.deleteTeacher(id);
		return ResponseEntity.ok("Teacher delete successfully");
	}
}