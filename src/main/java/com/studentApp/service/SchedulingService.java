package com.studentApp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.TeacherSubjectRegistrationRequest;
import com.studentApp.dto.response.ClassResponseDTO;
import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.entity.Class;
import com.studentApp.repository.ClassRepository;

@Service
public class SchedulingService {

	@Autowired
	private ClassRepository classRepository;

	// Phương thức từ ClassService (chuyển sang SchedulingService)
	public List<ClassResponseDTO> getAllClasses() {
		return classRepository.findAll().stream().map(this::mapToClassResponseDTO).collect(Collectors.toList());
	}

	// Các phương thức hiện có trong SchedulingService (giả định đã được triển khai)
	@Transactional
	public void registerSubjects(TeacherSubjectRegistrationRequest request) {
		// Triển khai logic đăng ký môn học cho giáo viên
	}

	@Transactional
	public void assignClasses(Long semesterId) {
		// Triển khai logic phân lớp
	}

	public List<SubjectResponse> getDepartmentSubjects(Long semesterId, Long deptId) {
		// Triển khai logic lấy danh sách môn học theo khoa
		return List.of(); // Giả định
	}

	public List<SubjectResponse> getTeacherSubjects(Long teacherId, Long semesterId) {
		// Triển khai logic lấy danh sách môn học của giáo viên
		return List.of(); // Giả định
	}

	@Transactional
	public void generateSchedule(Long semesterId) throws Exception {
		// Triển khai logic tạo lịch học tự động
	}

	@Transactional
	public ScheduleResponse addSchedule(ScheduleResponse scheduleResponse) {
		// Triển khai logic thêm lịch học
		return scheduleResponse; // Giả định
	}

	@Transactional
	public ScheduleResponse updateSchedule(Long id, ScheduleResponse scheduleResponse) {
		// Triển khai logic cập nhật lịch học
		return scheduleResponse; // Giả định
	}

	@Transactional
	public void deleteSchedule(Long id) {
		// Triển khai logic xóa lịch học
	}

	public List<ScheduleResponse> getScheduleByWeek(LocalDate startDate, LocalDate endDate) {
		// Triển khai logic lấy lịch học theo tuần
		return List.of(); // Giả định
	}

	public List<ScheduleResponse> getScheduleByDay(LocalDate date) {
		// Triển khai logic lấy lịch học theo ngày
		return List.of(); // Giả định
	}

	public List<ScheduleResponse> getScheduleByClass(Long classId) {
		// Triển khai logic lấy lịch học theo lớp
		return List.of(); // Giả định
	}

	// Phương thức ánh xạ từ Class entity sang ClassResponseDTO
	private ClassResponseDTO mapToClassResponseDTO(Class classEntity) {
		ClassResponseDTO dto = new ClassResponseDTO();
		dto.setClassCode(classEntity.getClassCode());
		dto.setClassName(classEntity.getClassName());
		dto.setSubjectName(classEntity.getSubject().getSubjectName());
		dto.setTeacherName(classEntity.getTeacher().getTeacherName());
		dto.setGroupCode(classEntity.getClassGroup().getGroupCode());
		dto.setStartDate(classEntity.getStartDate());
		dto.setEndDate(classEntity.getEndDate());
		dto.setClassroom(classEntity.getClassroom());
		dto.setShift(classEntity.getShift());
		dto.setPriority(classEntity.getPriority());
		return dto;
	}
}