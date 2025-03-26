package com.studentApp.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

	@Column(name = "description", length = 225)
	private String description;
}