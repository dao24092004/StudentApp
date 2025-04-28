package com.studentApp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.DepartmentCreationRequest;
import com.studentApp.dto.response.DepartmentResponse;
import com.studentApp.entity.Department;
import com.studentApp.repository.DepartmentRepository;

@Service
public class DepartmentService {
	@Autowired
	private DepartmentRepository departmentRepository;

	@Transactional
	public DepartmentResponse insertDepartment(DepartmentCreationRequest request) {
		Department department = new Department();
		department.setDeptCode(request.getDeptCode());
		department.setDeptName(request.getDeptName());
		department.setDescription(request.getDescription());
		department.setCreatedAt(LocalDateTime.now());
		department.setUpdatedAt(LocalDateTime.now());
		department = departmentRepository.save(department);
		return mapToDepartmentResponse(department);
	}

	@Transactional
	public DepartmentResponse updateDepartment(Long id, DepartmentCreationRequest request) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
		department.setDeptCode(request.getDeptCode());
		department.setDeptName(request.getDeptName());
		department.setDescription(request.getDescription());
		department.setUpdatedAt(LocalDateTime.now());
		department = departmentRepository.save(department);
		return mapToDepartmentResponse(department);
	}

	@Transactional
	public void deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
		departmentRepository.delete(department);
	}

	public List<DepartmentResponse> getAllDepartments() {
		return departmentRepository.findAll().stream().map(this::mapToDepartmentResponse).collect(Collectors.toList());
	}

	public DepartmentResponse getDepartmentById(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Department not found with id: " + id));
		return mapToDepartmentResponse(department);
	}

	private DepartmentResponse mapToDepartmentResponse(Department department) {
		DepartmentResponse response = new DepartmentResponse();
		response.setId(department.getId());
		response.setDeptCode(department.getDeptCode());
		response.setDeptName(department.getDeptName());
		response.setDescription(department.getDescription());
		response.setCreatedAt(department.getCreatedAt());
		response.setUpdatedAt(department.getUpdatedAt());
		return response;
	}
}