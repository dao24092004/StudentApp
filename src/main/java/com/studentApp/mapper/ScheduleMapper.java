package com.studentApp.mapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.entity.Registration;
import com.studentApp.entity.Schedule;
import com.studentApp.repository.RegistrationRepository;

@Component
public class ScheduleMapper {

	@Autowired
	private RegistrationRepository registrationRepository;

	public ScheduleResponse toDTO(Schedule schedule) {
		ScheduleResponse dto = new ScheduleResponse();
		dto.setId(schedule.getId());

		// Ánh xạ thông tin lớp học
		if (schedule.getClassEntity() != null) {
			dto.setClassCode(schedule.getClassEntity().getClassCode());
			dto.setClassName(schedule.getClassEntity().getClassName());

			// Ánh xạ thông tin giáo viên
			if (schedule.getClassEntity().getTeacher() != null) {
				dto.setTeacherName(schedule.getClassEntity().getTeacher().getTeacherName());
			}
		}

		// Ánh xạ thông tin môn học
		if (schedule.getSubject() != null) {
			dto.setSubjectCode(schedule.getSubject().getSubjectCode());
			dto.setSubjectName(schedule.getSubject().getSubjectName());
		}

		// Ánh xạ thông tin lịch học
		dto.setDayOfWeek(schedule.getDayOfWeek());
		dto.setSlot(schedule.getSlot());
		dto.setPeriod(schedule.getPeriod());
		dto.setStartTime(schedule.getStartTime());
		dto.setEndTime(schedule.getEndTime());

		// Ánh xạ thông tin sinh viên (nếu có)
		List<Registration> registrations = registrationRepository
				.findByClassEntityId(schedule.getClassEntity().getId());
		if (!registrations.isEmpty()) {
			Registration registration = registrations.get(0); // Lấy sinh viên đầu tiên (nếu cần tất cả thì có thể trả
																// về danh sách)
			if (registration.getStudent() != null) {
				dto.setStudentName(registration.getStudent().getStudentName());
			}
		}

		return dto;
	}

	public Schedule toEntity(ScheduleResponse dto) {
		Schedule schedule = new Schedule();
		schedule.setId(dto.getId());
		// Các ánh xạ khác nếu cần thiết (dùng trong tạo mới hoặc cập nhật)
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
		return schedule;
	}

	public void updateEntity(Schedule schedule, ScheduleResponse dto) {
		schedule.setDayOfWeek(dto.getDayOfWeek());
		schedule.setSlot(dto.getSlot());
		schedule.setPeriod(dto.getPeriod());
		schedule.setStartTime(dto.getStartTime());
		schedule.setEndTime(dto.getEndTime());
	}
}