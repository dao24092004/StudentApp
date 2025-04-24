package com.studentApp.entity;

import java.time.LocalDate;

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
@Table(name = "tbl_registration")
@Data
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "class_id", nullable = false)
	private Class classEntity;

	@ManyToOne
	@JoinColumn(name = "student_id", nullable = false)
	private Student student;

	@ManyToOne
	@JoinColumn(name = "semester_id", nullable = false)
	private Semester semester;

	@Column(name = "registration_date")
	private LocalDate registrationDate;

	private String status;
}