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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.GradeCreationRequest;
import com.studentApp.dto.request.GradeUpdateRequest;
import com.studentApp.dto.response.GradeResponse;
import com.studentApp.service.GradeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(GradeController.class);

    @GetMapping
    @PreAuthorize("hasAuthority('GRADE_VIEW')")
    @Operation(summary = "Lấy danh sách tất cả điểm")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lấy danh sách điểm thành công"),
        @ApiResponse(responseCode = "403", description = "Không có quyền truy cập", content = @io.swagger.v3.oas.annotations.media.Content)
    })
    public ResponseEntity<List<GradeResponse>> getAllGrades() {
        log.info("Getting all grades");
        List<GradeResponse> response = gradeService.getAllGrades();
        log.info("Retrieved {} grades", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('GRADE_VIEW')")
    @Operation(summary = "Get grade by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grade found successfully"),
        @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    public ResponseEntity<GradeResponse> getGradeById(@PathVariable Long id) {
        GradeResponse gradeResponse = gradeService.getGradeById(id);
        return ResponseEntity.ok(gradeResponse);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('GRADE_CREATE')")
    @Operation(summary = "Create a new grade")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Grade created successfully"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<GradeResponse> createGrade(@RequestBody GradeCreationRequest request) {
        log.info("Creating grade for studentId: {}, classId: {}", request.getStudent_id(), request.getClass_id());
        GradeResponse grade = gradeService.createGrade(request);
        return ResponseEntity.ok(grade);
    }
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('GRADE_UPDATE')")
    @Operation(summary = "Update a grade")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Grade updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Access denied")
    })
    public ResponseEntity<GradeResponse> updateGrade(@PathVariable Long id, @RequestBody GradeUpdateRequest request) {
        GradeResponse gradeResponse = gradeService.updateGrade(id, request);
        return ResponseEntity.ok(gradeResponse);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('GRADE_DELETE')")
    @Operation(summary = "Delete a grade")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Grade deleted successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "403", description = "Access denied") 
    })
    public ResponseEntity<String> deleteGrade(@PathVariable Long id) {
        gradeService.deleteGrade(id);  // Gọi service để xóa grade
        return ResponseEntity.ok("Grade deleted successfully");
    }

}
