package com.studentApp.controller;

import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.request.StudentUpdateRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.service.StudentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.media.Content;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/student")
    @PreAuthorize("hasAuthority('CLASS_VIEW')")
    public ResponseEntity<List<StudentResponse>> getAllStudent() {
        List<StudentResponse> response = studentService.getAllStudent();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/student/{id}")
    @PreAuthorize("hasAuthority('CLASS_VIEW')")
    public ResponseEntity<StudentResponse> getIDStudent(@PathVariable long id) {
        StudentResponse studentResponse = studentService.getIDStudent(id);
        return ResponseEntity.ok(studentResponse);
    }

    @PostMapping("/student")
    @PreAuthorize("hasAuthority('CLASS_CREATE')")
    @Operation(summary = "Create a new student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<StudentResponse> createStudent(@RequestBody StudentCreationRequest request) {
        StudentResponse student = studentService.createStudent(request);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/student/{id}")
    @PreAuthorize("hasAuthority('CLASS_VIEW')")
    @Operation(summary = "Update a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Student updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentUpdateRequest request) {
        StudentResponse studentResponse = studentService.updateStudent(id, request);
        return ResponseEntity.ok(studentResponse);
    }

    @DeleteMapping("/student/delete/{id}")
    @PreAuthorize("hasAuthority('CLASS_UPDATE')")
    @Operation(summary = "Delete a student", description = "Delete a student by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Student deleted successfully", content = @Content),
		    @ApiResponse(responseCode = "404", description = "Student not found", content = @Content),
		    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content) 
    })
    public ResponseEntity<String> deleteStudEntity9Entity(@PathVariable Long id){
            studentService.deleteStudent(id);
            return ResponseEntity.ok("Student deleted successfully");
    }
}