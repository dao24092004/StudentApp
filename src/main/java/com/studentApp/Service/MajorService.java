package com.studentApp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.dto.request.MajorCreationRequest;
import com.studentApp.dto.response.MajorResponse;
import com.studentApp.entity.Department;
import com.studentApp.entity.Major;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.MajorRepository;

@Service
public class MajorService {

	@Autowired
	private MajorRepository majorRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	public MajorResponse createMajor(MajorCreationRequest request) {
		if (majorRepository.findByMajorCode(request.getMajorCode()) != null) {
			throw new AppException(ErrorCode.MAJOR_CODE_ALREADY_EXISTS);
		}

		Department department = departmentRepository.findByDeptName(request.getDeptName())
				.orElseThrow(() -> new AppException(ErrorCode.DEPARTMENT_NOT_FOUND));

		Major major = new Major();
		major.setMajorCode(request.getMajorCode());
		major.setMajorName(request.getMajorName());
		major.setDepartment(department);
		major.setDescription(request.getDescription());
		major.setCreatedAt(LocalDateTime.now());
		major.setUpdatedAt(LocalDateTime.now());

		major = majorRepository.save(major);
		return toMajorResponse(major);
	}

	private MajorResponse toMajorResponse(Major major) {
		MajorResponse response = new MajorResponse();
		response.setId(major.getId());
		response.setMajorCode(major.getMajorCode());
		response.setMajorName(major.getMajorName());
		response.setDeptId(major.getDepartment().getId());
		response.setDescription(major.getDescription());
		response.setCreatedAt(major.getCreatedAt());
		response.setUpdatedAt(major.getUpdatedAt());
		return response;
	}
}