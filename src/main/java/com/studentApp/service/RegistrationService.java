package com.studentApp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.studentApp.entity.Registration;
import com.studentApp.entity.Schedule;
import com.studentApp.repository.RegistrationRepository;
import com.studentApp.repository.ScheduleRepository;

// Service để xử lý logic đăng ký học phần
@Service
public class RegistrationService {
	private final RegistrationRepository registrationRepository;
	private final ScheduleRepository scheduleRepository;

	// Constructor
	public RegistrationService(RegistrationRepository registrationRepository, ScheduleRepository scheduleRepository) {
		this.registrationRepository = registrationRepository;
		this.scheduleRepository = scheduleRepository;
	}

	// Đăng ký học phần cho sinh viên
	public void registerClass(Long studentId, Long classId, Long semesterId) throws Exception {
		// Lấy danh sách các lớp sinh viên đã đăng ký trong kỳ học
		List<Registration> registrations = registrationRepository.findByStudentIdAndSemesterId(studentId, semesterId);

		// Kiểm tra giới hạn 3 lớp
		if (registrations.size() >= 3) {
			throw new Exception("Sinh viên đã đăng ký đủ 3 lớp học");
		}

		// Lấy lịch của lớp mới
		List<Schedule> newClassSchedules = scheduleRepository.findByClassId(classId);

		// Kiểm tra trùng lịch
		for (Registration registration : registrations) {
			List<Schedule> existingSchedules = scheduleRepository.findByClassId(registration.getClassId());
			for (Schedule existing : existingSchedules) {
				for (Schedule newSchedule : newClassSchedules) {
					if (existing.getDayOfWeek().equals(newSchedule.getDayOfWeek())
							&& existing.getSlot().equals(newSchedule.getSlot())
							&& existing.getStartTime().toLocalDate().equals(newSchedule.getStartTime().toLocalDate())) {
						throw new Exception("Lịch học bị trùng với lớp " + classId);
					}
				}
			}
		}

		// Lưu đăng ký mới
		Registration registration = new Registration();
		registration.setStudentId(studentId);
		registration.setClassId(classId);
		registration.setSemesterId(semesterId);
		registration.setRegistrationDate(LocalDate.now());
		registration.setStatus("Registered");
		registrationRepository.save(registration);
	}
}