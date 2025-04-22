package com.studentApp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Định nghĩa entity cho bảng tbl_schedule
@Entity
@Table(name = "tbl_schedule")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Schedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "class_id")
	private Long classId;

	@Column(name = "subject_id")
	private Long subjectId;

	@Column(name = "day_of_week")
	private String dayOfWeek;

	@Column(name = "slot")
	private Integer slot;

	@Column(name = "period")
	private Integer period;

	@Column(name = "start_time")
	private LocalDateTime startTime;

	@Column(name = "end_time")
	private LocalDateTime endTime;

}