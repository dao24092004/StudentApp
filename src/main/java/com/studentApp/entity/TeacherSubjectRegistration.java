package com.studentApp.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_teacher_subject_registration")
@Data
public class TeacherSubjectRegistration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "teacher_id", nullable = false)
	private Teacher teacher;

	@ManyToOne
	@JoinColumn(name = "subject_id", nullable = false)
	private Subject subject;

	@ManyToOne
	@JoinColumn(name = "semester_id", nullable = false)
	private Semester semester;
}