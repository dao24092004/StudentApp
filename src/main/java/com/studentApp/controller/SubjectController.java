package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    // PUT: Cập nhật subject
    // Yêu cầu quyền SUBJECT_UPDATE
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_UPDATE')")
    public ResponseEntity<SubjectResponseDTO> updateSubject(@PathVariable Long id, @RequestBody SubjectRequestDTO dto) {
        return ResponseEntity.ok(subjectService.updateSubject(id, dto));
    }

    // DELETE: Xóa subject
    // Yêu cầu quyền SUBJECT_DELETE
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUBJECT_DELETE')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.noContent().build();
    }
}