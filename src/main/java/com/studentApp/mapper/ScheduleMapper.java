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
        dto.setTeacherId(schedule.getTeacher() != null ? schedule.getTeacher().getId() : null);
        dto.setRoomId(schedule.getRoom() != null ? schedule.getRoom().getId() : null);
        dto.setSemesterId(schedule.getSemester() != null ? schedule.getSemester().getId() : null);
        dto.setDayOfWeek(schedule.getDayOfWeek());
        dto.setSlot(schedule.getSlot());
        dto.setPeriod(schedule.getPeriod());
        dto.setStartTime(schedule.getStartTime());
        dto.setEndTime(schedule.getEndTime());
        dto.setStatus(schedule.getStatus());
        return dto;
    }

    public Schedule toEntity(ScheduleResponse dto) {
        Schedule schedule = new Schedule();
        schedule.setId(dto.getId());
        schedule.setDayOfWeek(dto.getDayOfWeek());
        schedule.setSlot(dto.getSlot());
        schedule.setPeriod(dto.getPeriod());
        schedule.setStartTime(dto.getStartTime());
        schedule.setEndTime(dto.getEndTime());
        schedule.setStatus(dto.getStatus());
        // Note: classEntity, subject, teacher, room, semester need to be set separately
        return schedule;
    }

    public void updateEntity(Schedule schedule, ScheduleResponse dto) {
        if (dto.getDayOfWeek() != null) schedule.setDayOfWeek(dto.getDayOfWeek());
        if (dto.getSlot() != null) schedule.setSlot(dto.getSlot());
        if (dto.getPeriod() != null) schedule.setPeriod(dto.getPeriod());
        if (dto.getStartTime() != null) schedule.setStartTime(dto.getStartTime());
        if (dto.getEndTime() != null) schedule.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) schedule.setStatus(dto.getStatus());
        // Note: Update classEntity, subject, teacher, room, semester if needed
    }
}