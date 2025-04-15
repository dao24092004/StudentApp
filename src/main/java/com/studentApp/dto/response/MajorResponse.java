package com.studentApp.dto.response;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.studentApp.repository.MajorRepository;

import lombok.Data;

@Data
public class MajorResponse {
	private Long id;
	private String majorCode;
	private String majorName;
	private Long deptId;
	private String deptName; // Thêm trường deptName
	private String description;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	@Autowired
private MajorRepository majorRepository;

}