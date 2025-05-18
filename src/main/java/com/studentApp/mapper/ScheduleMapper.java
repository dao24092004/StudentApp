package com.studentApp.mapper;

import org.springframework.stereotype.Component;

import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.entity.Schedule;

@Component
public class ScheduleMapper {

	public ScheduleResponse toDTO(Schedule schedule) {
		ScheduleResponse dto = new ScheduleResponse();
		dto.setId(schedule.getId());
		dto.setClassId(schedule.getClassEntity() != null ? schedule.getClassEntity().getId() : null);
		dto.setSubjectId(schedule.getSubject() != null ? schedule.getSubject().getId() : null);
		dto.setDayOfWeek(schedule.getDayOfWeek());
		dto.setSlot(schedule.getSlot());
		dto.setPeriod(schedule.getPeriod());
		dto.setStartTime(schedule.getStartTime());
		dto.setEndTime(schedule.getEndTime());
		dto.setSemesterId(schedule.getSemester() != null ? schedule.getSemester().getId() : null);
		dto.setWeek(schedule.getWeek());
		dto.setStatus(schedule.getStatus());
		return dto;
	}

	public Schedule toEntity(ScheduleResponse dto) {
		Schedule schedule = new Schedule();
		schedule.setId(dto.getId());
		// Note: Class, Subject, Semester need to be set separately if needed
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
		schedule.setWeek(dto.getWeek());
		schedule.setStatus(dto.getStatus());
		return schedule;
	}

	public void updateEntity(Schedule schedule, ScheduleResponse dto) {
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
		schedule.setWeek(dto.getWeek());
		schedule.setStatus(dto.getStatus());
		// Note: Update classEntity, subject, semester if needed
	}
}
