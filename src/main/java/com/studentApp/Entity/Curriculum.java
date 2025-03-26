package com.studentApp.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_curriculum")
@Data
public class Curriculum {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "curriculum_code", nullable = false, unique = true, length = 20)
	private String curriculumCode;

	@Column(name = "curriculum_name", nullable = false, length = 100)
	private String curriculumName;

	@ManyToMany(mappedBy = "curriculum")
	private Set<Major> majors = new HashSet<>();

	@Column(name = "description", length = 250)
	private String description;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;
}