package com.studentApp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.SubjectRequestDTO;
import com.studentApp.dto.response.SubjectResponseDTO;
import com.studentApp.entity.Department;
import com.studentApp.entity.Subject;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.SubjectRepository;

@Service
public class SubjectService {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    @Transactional
    public SubjectResponseDTO createSubject(SubjectRequestDTO dto) {
        Subject subject = new Subject();
        subject.setSubjectCode(dto.getSubjectCode());
        subject.setSubjectName(dto.getSubjectName());
        subject.setCredits(dto.getCredits());
        Department department = departmentRepository.findByDeptName(dto.getDeptName())
                .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDeptName()));
        subject.setDeptId(department.getId());
        subject.setDescription(dto.getDescription());
        subject.setTheoryPeriods(dto.getTheoryPeriods());
        subject.setPracticalPeriods(dto.getPracticalPeriods());
        subject = subjectRepository.save(subject);
        return mapToSubjectResponseDTO(subject);
    }

    public List<SubjectResponseDTO> getAllSubjects() {
        return subjectRepository.findAll().stream().map(this::mapToSubjectResponseDTO).collect(Collectors.toList());
    }

    @Transactional
    public SubjectResponseDTO updateSubject(Long id, SubjectRequestDTO dto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));

        subject.setSubjectCode(dto.getSubjectCode());
        subject.setSubjectName(dto.getSubjectName());
        subject.setCredits(dto.getCredits());
        Department department = departmentRepository.findByDeptName(dto.getDeptName())
                .orElseThrow(() -> new RuntimeException("Department not found: " + dto.getDeptName()));
        subject.setDeptId(department.getId());
        subject.setDescription(dto.getDescription());
        subject.setTheoryPeriods(dto.getTheoryPeriods());
        subject.setPracticalPeriods(dto.getPracticalPeriods());

        subject = subjectRepository.save(subject);
        return mapToSubjectResponseDTO(subject);
    }

    @Transactional
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + id));
        subjectRepository.delete(subject);
    }

    private SubjectResponseDTO mapToSubjectResponseDTO(Subject subject) {
        SubjectResponseDTO dto = new SubjectResponseDTO();
        dto.setId(subject.getId());
        dto.setSubjectCode(subject.getSubjectCode());
        dto.setSubjectName(subject.getSubjectName());
        dto.setCredits(subject.getCredits());
        Department department = departmentRepository.findById(subject.getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found: " + subject.getDeptId()));
        dto.setDeptName(department.getDeptName());
        dto.setDescription(subject.getDescription());
        dto.setTheoryPeriods(subject.getTheoryPeriods());
        dto.setPracticalPeriods(subject.getPracticalPeriods());
        return dto;
    }
}