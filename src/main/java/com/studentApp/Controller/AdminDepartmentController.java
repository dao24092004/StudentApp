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

import com.studentApp.dto.request.DepartmentCreationRequest;
import com.studentApp.dto.response.DepartmentResponse;
import com.studentApp.service.DepartmentService;

@RestController
@RequestMapping("/admin/departments")
public class AdminDepartmentController {

	@Autowired
	private DepartmentService departmentService;

	// Get all departments
	@GetMapping("/getall")
	@PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
	public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
		List<DepartmentResponse> response = departmentService.getAllDepartments();
		return ResponseEntity.ok(response);
	}

	// Get department by ID
	@GetMapping("/{id}")
	@PreAuthorize("hasAuthority('DEPARTMENT_VIEW')")
	public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
		DepartmentResponse response = departmentService.getDepartmentById(id);
		return ResponseEntity.ok(response);
	}

	// Create a new department
	@PostMapping("/insert")
	@PreAuthorize("hasAuthority('DEPARTMENT_CREATE')")
	public ResponseEntity<DepartmentResponse> insertDepartment(@RequestBody DepartmentCreationRequest request) {
		DepartmentResponse response = departmentService.insertDepartment(request);
		return ResponseEntity.ok(response);
	}

	// Update a department
	@PutMapping("/update/{id}")
	@PreAuthorize("hasAuthority('DEPARTMENT_UPDATE')")
	public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long id,
			@RequestBody DepartmentCreationRequest request) {
		DepartmentResponse response = departmentService.updateDepartment(id, request);
		return ResponseEntity.ok(response);
	}

	// Delete a department
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasAuthority('DEPARTMENT_DELETE')")
	public ResponseEntity<String> deleteDepartment(@PathVariable Long id) {
		departmentService.deleteDepartment(id);
		return ResponseEntity.ok("Department deleted successfully");
	}
}