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

import com.studentApp.dto.request.DepartmentCreationRequest;
import com.studentApp.dto.request.DepartmentRequestDTO;
import com.studentApp.dto.response.DepartmentResponse;
import com.studentApp.service.DepartmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Autowired
	private DepartmentService departmentService;
    
    @PostMapping("/create")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
    @Operation(summary = "Create a department", description = "Create an existing department by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Department create successfully"),
			@ApiResponse(responseCode = "404", description = "Department not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
    public ResponseEntity<DepartmentResponse> insertDepartment(@RequestBody DepartmentCreationRequest dto) {
        return ResponseEntity.ok(departmentService.insertDepartment(dto));
    }

    @PostMapping("/update/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
    @Operation(summary = "Update a department", description = "Update an existing department by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Department update successfully"),
			@ApiResponse(responseCode = "404", description = "Department not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
	public ResponseEntity<DepartmentResponse> updateDepartment(@PathVariable Long id, @RequestBody DepartmentCreationRequest dto) {
        return ResponseEntity.ok(departmentService.updateDepartment(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('CLASS_DELETE')")
    @Operation(summary = "Delete a department", description = "Delete an existing department by ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Department delete successfully"),
			@ApiResponse(responseCode = "404", description = "Department not found"),
			@ApiResponse(responseCode = "400", description = "Invalid input data"),
			@ApiResponse(responseCode = "403", description = "Access denied") })
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/")
    @PreAuthorize("hasAuthority('CLASS_VIEW')")
    public ResponseEntity<List<DepartmentResponse>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
}
