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
@Table(name = "tbl_schedule")
@Data
public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "class_id")
	private Class classEntity;

	@ManyToOne
	@JoinColumn(name = "subject_id")
	private Subject subject;

	@Column(name = "day_of_week", nullable = false)
	private String dayOfWeek;

	private Integer slot;

	private Integer period;

	@Column(name = "start_time", nullable = false)
	private LocalDateTime startTime;

	@Column(name = "end_time", nullable = false)
	private LocalDateTime endTime;
}