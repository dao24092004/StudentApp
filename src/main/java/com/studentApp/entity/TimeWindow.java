package com.studentApp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_time_window")
public class TimeWindow {
	@Id
	@Column(name = "id_class")
	private Long classId;

	@Id
	@Column(name = "day_of_week")
	private String dayOfWeek;

	@Id
	private Integer slot;

	@Column(name = "created_date", nullable = false)
	private LocalDate createdDate;
}