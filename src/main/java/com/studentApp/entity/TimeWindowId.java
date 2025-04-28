package com.studentApp.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimeWindowId implements Serializable {
	private Long idClass; // Changed from Integer to Long
	private String dayOfWeek;
	private Integer slot;
}