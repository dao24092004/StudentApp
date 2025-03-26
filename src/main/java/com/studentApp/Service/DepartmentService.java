package com.studentApp.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.Entity.Department;
import com.studentApp.Repository.DepartmentRepository;
import com.studentApp.dto.request.DepartmentCreationRequest;
import com.studentApp.dto.response.DepartmentResponse;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository departmentRepository;

	public List<DepartmentResponse> getAllDepartments() {
		return departmentRepository.findAll().stream().map(this::toDepartmentResponse).collect(Collectors.toList());
	}

	public DepartmentResponse getDepartmentById(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
		return toDepartmentResponse(department);
	}

	public DepartmentResponse insertDepartment(DepartmentCreationRequest request) {
		if (departmentRepository.findByDeptName(request.getDeptName()) != null) {
			throw new AppException(ErrorCode.DEPARTMENT_ALREADY_EXISTS);
		}

		Department department = new Department();
		department.setDeptCode(request.getDeptCode());
		department.setDeptName(request.getDeptName());
		department.setDescription(request.getDescription());
		department.setCreatedAt(LocalDateTime.now());
		department.setUpdatedAt(LocalDateTime.now());

		department = departmentRepository.save(department);
		return toDepartmentResponse(department);
	}

	public DepartmentResponse updateDepartment(Long id, DepartmentCreationRequest request) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

		// Kiểm tra xem tên phòng ban mới có bị trùng không (trừ chính nó)
		Department existingDepartment = departmentRepository.findByDeptName(request.getDeptName());
		if (existingDepartment != null && !existingDepartment.getId().equals(id)) {
			throw new AppException(ErrorCode.DEPARTMENT_ALREADY_EXISTS);
		}

		department.setDeptCode(request.getDeptCode());
		department.setDeptName(request.getDeptName());
		department.setDescription(request.getDescription());
		department.setUpdatedAt(LocalDateTime.now());

		department = departmentRepository.save(department);
		return toDepartmentResponse(department);
	}

	public void deleteDepartment(Long id) {
		Department department = departmentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));
		departmentRepository.delete(department);
	}

	private DepartmentResponse toDepartmentResponse(Department department) {
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