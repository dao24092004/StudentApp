package com.studentApp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_major")
@Data
public class Major {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "major_code", nullable = false, unique = true, length = 20)
	private String majorCode;

	@Column(name = "major_name", nullable = false, length = 100)
	private String majorName;

	@Column(name = "dept_id", nullable = false)
	private Long deptId;

	@ManyToOne
	@JoinColumn(name = "curriculum_id")
	private Curriculum curriculum;

	@Column(length = 250)
	private String description;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}