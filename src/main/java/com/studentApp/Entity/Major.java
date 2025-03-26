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

	@ManyToOne
	@JoinColumn(name = "dept_id", nullable = false) // Khóa ngoại tới tbl_department
	private Department department;

	@ManyToOne
	@JoinColumn(name = "curriculum_id", unique = true) // Khóa ngoại tới tbl_curriculum, unique
	private Curriculum curriculum;

	@Column(name = "description", length = 250)
	private String description;
	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;

}