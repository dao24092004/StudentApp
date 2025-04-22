package com.studentApp.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_time_window")
@IdClass(TimeWindowId.class)
@Data
public class TimeWindow {

	@Id
	@Column(name = "id_class")
	private Integer idClass;

	@Id
	@Column(name = "day_of_week")
	private String dayOfWeek;

	@Id
	@Column(name = "slot")
	private Integer slot;

	@Column(name = "created_date", nullable = false)
	private LocalDate createdDate;

	@ManyToOne
	@JoinColumn(name = "id_class", insertable = false, updatable = false)
	private Class clazz;
}