package com.studentApp.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class TimeWindowId implements Serializable {
	private Integer idClass;
	private String dayOfWeek;
	private Integer slot;
}