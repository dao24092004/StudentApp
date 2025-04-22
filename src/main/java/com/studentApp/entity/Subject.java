package com.studentApp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_subject")
public class Subject {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "subject_code", nullable = false)
	private String subjectCode;

	@Column(name = "subject_name", nullable = false)
	private String subjectName;

	@Column(nullable = false)
	private Integer credits;

	private String description;

	@Column(name = "semester_id", nullable = false)
	private Long semesterId;

	@Column(name = "dept_id", nullable = false)
	private Long deptId;
}