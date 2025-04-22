package com.studentApp.mapper;

import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.entity.Schedule;

public class ScheduleMapper {

	public static ScheduleResponse toDTO(Schedule schedule) {
		ScheduleResponse dto = new ScheduleResponse();
		dto.setId(schedule.getId());
		dto.setClassId(schedule.getClassId());
		dto.setSubjectId(schedule.getSubjectId());
		dto.setDayOfWeek(schedule.getDayOfWeek());
		dto.setSlot(schedule.getSlot());
		dto.setPeriod(schedule.getPeriod());
		dto.setStartTime(schedule.getStartTime());
		dto.setEndTime(schedule.getEndTime());
		return dto;
	}

	public static Schedule toEntity(ScheduleResponse dto) {
		Schedule schedule = new Schedule();
		schedule.setId(dto.getId());
		schedule.setClassId(dto.getClassId());
		schedule.setSubjectId(dto.getSubjectId());
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
		return schedule;
	}

	public static void updateEntity(Schedule schedule, ScheduleResponse dto) {
		schedule.setClassId(dto.getClassId());
		schedule.setSubjectId(dto.getSubjectId());
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
	}
}