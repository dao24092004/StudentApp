package com.studentApp.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.entity.Registration;
import com.studentApp.entity.Schedule;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.RegistrationRepository;
import com.studentApp.repository.ScheduleRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.StudentRepository;

// Service để xử lý logic đăng ký học phần
@Service
public class RegistrationService {

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private ScheduleRepository scheduleRepository;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private SemesterRepository semesterRepository;

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
			List<Schedule> existingSchedules = scheduleRepository.findByClassId(registration.getClassEntity().getId());
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
		registration.setStudent(
				studentRepository.findById(studentId).orElseThrow(() -> new Exception("Sinh viên không tồn tại")));
		registration.setClassEntity(
				classRepository.findById(classId).orElseThrow(() -> new Exception("Lớp học không tồn tại")));
		registration.setSemester(
				semesterRepository.findById(semesterId).orElseThrow(() -> new Exception("Kỳ học không tồn tại")));
		registration.setRegistrationDate(LocalDate.now());
		registration.setStatus("Registered");
		registrationRepository.save(registration);
	}
}