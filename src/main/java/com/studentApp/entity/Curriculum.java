package com.studentApp.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_curriculum")
@Data
public class Curriculum {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "curriculum_seq")
	@SequenceGenerator(name = "curriculum_seq", sequenceName = "tbl_curriculum_id_seq", allocationSize = 1)
	private Long id;

	@Column(name = "curriculum_code", nullable = false, unique = true, length = 20)
	private String curriculumCode;

	@Column(name = "curriculum_name", nullable = false, length = 100)
	private String curriculumName;

	@OneToMany(mappedBy = "curriculum")
	private List<Major> majors;

	@Column(name = "description", length = 250)
	private String description;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;
}