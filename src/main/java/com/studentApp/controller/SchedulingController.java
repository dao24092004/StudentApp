package com.studentApp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.AssignClassesRequest;
import com.studentApp.dto.request.ScheduleRequest;
import com.studentApp.dto.request.TeacherSubjectRegistrationRequest;
import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.service.SchedulingService;

@RestController
@RequestMapping("/api/classes")
public class SchedulingController {

	private static final Logger logger = LoggerFactory.getLogger(SchedulingController.class);

	private final SchedulingService schedulingService;

	public SchedulingController(SchedulingService schedulingService) {
		this.schedulingService = schedulingService;
	}

	@PostMapping("/teachers/register-subjects")
	@PreAuthorize("hasAuthority('TEACHER_REGISTER')")
	public ResponseEntity<Map<String, String>> registerSubjects(
			@RequestBody TeacherSubjectRegistrationRequest request) {
		logger.info("Nhận yêu cầu đăng ký môn học cho teacherId={}", request.getTeacherId());
		try {
			schedulingService.registerSubjects(request);
			logger.info("Đăng ký môn học thành công cho teacherId={}", request.getTeacherId());
			return ResponseEntity.ok(Map.of("status", "success"));
		} catch (Exception e) {
			logger.error("Lỗi khi đăng ký môn học: ", e);
			return ResponseEntity.status(500)
					.body(Map.of("status", "error", "message", "Không thể đăng ký môn học: " + e.getMessage()));
		}
	}

	@PostMapping("/assign-classes")
	@PreAuthorize("hasAuthority('CLASS_ASSIGN')")
	public ResponseEntity<Map<String, String>> assignClasses(@RequestBody AssignClassesRequest request) {
		logger.info("Nhận yêu cầu phân công lớp học cho semesterId={}", request.getSemesterId());
		try {
			schedulingService.assignClasses(request.getSemesterId());
			logger.info("Phân công lớp học thành công cho semesterId={}", request.getSemesterId());
			return ResponseEntity.ok(Map.of("status", "success"));
		} catch (Exception e) {
			logger.error("Lỗi khi phân công lớp học: ", e);
			return ResponseEntity.status(500)
					.body(Map.of("status", "error", "message", "Không thể phân công lớp học: " + e.getMessage()));
		}
	}

	@GetMapping("/department-subjects")
	@PreAuthorize("hasAnyAuthority('CLASS_VIEW')")
	public ResponseEntity<List<SubjectResponse>> getDepartmentSubjects(@RequestParam Long semesterId,
			@RequestParam Long deptId) {
		logger.info("Lấy danh sách môn học cho semesterId={} và deptId={}", semesterId, deptId);
		try {
			List<SubjectResponse> subjects = schedulingService.getDepartmentSubjects(semesterId, deptId);
			logger.info("Lấy thành công {} môn học cho semesterId={} và deptId={}", subjects.size(), semesterId,
					deptId);
			return ResponseEntity.ok(subjects);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy danh sách môn học: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/teachers/{teacherId}/subjects")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<SubjectResponse>> getTeacherSubjects(@PathVariable Long teacherId,
			@RequestParam Long semesterId) {
		logger.info("Lấy danh sách môn học của giảng viên teacherId={} cho semesterId={}", teacherId, semesterId);
		try {
			List<SubjectResponse> subjects = schedulingService.getTeacherSubjects(teacherId, semesterId);
			logger.info("Lấy thành công {} môn học cho teacherId={} và semesterId={}", subjects.size(), teacherId,
					semesterId);
			return ResponseEntity.ok(subjects);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy danh sách môn học của giảng viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@PostMapping("/schedules/generate")
	@PreAuthorize("hasAuthority('SCHEDULE_CREATE')")
	public ResponseEntity<Map<String, String>> generateSchedule(@RequestBody ScheduleRequest request) {
		logger.info("Nhận yêu cầu tạo lịch với semesterId={}", request.getSemesterId());
		try {
			schedulingService.generateSchedule(request.getSemesterId().toString());
			logger.info("Tạo lịch thành công cho semesterId={}", request.getSemesterId());
			return ResponseEntity.ok(Map.of("status", "success"));
		} catch (Exception e) {
			logger.error("Lỗi khi tạo lịch: {}", e.getMessage(), e);
			return ResponseEntity.status(500)
					.body(Map.of("status", "error", "message", "Không thể tạo lịch: " + e.getMessage()));
		}
	}

	@PutMapping("/update/schedules/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
	public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
			@RequestBody ScheduleResponse scheduleResponse) {
		logger.info("Nhận yêu cầu cập nhật lịch với id={}", id);
		try {
			ScheduleResponse updatedSchedule = schedulingService.updateSchedule(id, scheduleResponse);
			logger.info("Cập nhật lịch thành công cho id={}", id);
			return ResponseEntity.ok(updatedSchedule);
		} catch (Exception e) {
			logger.error("Lỗi khi cập nhật lịch: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@DeleteMapping("/delete/schedules/{id}")
	@PreAuthorize("hasAuthority('CLASS_DELETE')")
	public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id) {
		logger.info("Nhận yêu cầu xóa lịch với id={}", id);
		try {
			schedulingService.deleteSchedule(id);
			logger.info("Xóa lịch thành công cho id={}", id);
			return ResponseEntity.ok(Map.of("status", "success"));
		} catch (Exception e) {
			logger.error("Lỗi khi xóa lịch: ", e);
			return ResponseEntity.status(500)
					.body(Map.of("status", "error", "message", "Không thể xóa lịch: " + e.getMessage()));
		}
	}

	@GetMapping("/schedules/week")
	@PreAuthorize("hasAnyAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByWeek(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		logger.info("Lấy lịch theo tuần từ {} đến {}", startDate, endDate);
		try {
			List<ScheduleResponse> schedules = schedulingService.getScheduleByWeek(startDate, endDate);
			logger.info("Lấy thành công {} lịch theo tuần", schedules.size());
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo tuần: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/schedules/day")
	@PreAuthorize("hasAnyAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByDay(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		logger.info("Lấy lịch theo ngày: {}", date);
		try {
			List<ScheduleResponse> schedules = schedulingService.getScheduleByDay(date);
			logger.info("Lấy thành công {} lịch theo ngày", schedules.size());
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo ngày: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/schedules/class/{classId}")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByClass(@PathVariable Long classId) {
		logger.info("Lấy lịch cho lớp classId={}", classId);
		try {
			List<ScheduleResponse> schedules = schedulingService.getScheduleByClass(classId);
			logger.info("Lấy thành công {} lịch cho classId={}", schedules.size(), classId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch cho lớp: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/students/{studentId}/schedules")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getStudentSchedule(@PathVariable Long studentId,
			@RequestParam Long semesterId, @RequestParam(required = false) Integer week,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false, defaultValue = "CONFIRMED") String status) {
		logger.info(
				"Lấy lịch cho sinh viên studentId={} và semesterId={} với week={} hoặc từ {} đến {}, subjectId={}, status={}",
				studentId, semesterId, week, startDate, endDate, subjectId, status);
		try {
			List<ScheduleResponse> schedules;
			if (week != null) {
				logger.debug("Gọi service với week: {}", week);
				schedules = schedulingService.getStudentSchedule(studentId, semesterId, week, subjectId, status);
			} else if (startDate != null && endDate != null) {
				logger.debug("Gọi service với startDate: {} và endDate: {}", startDate, endDate);
				schedules = schedulingService.getStudentScheduleByWeek(studentId, startDate, endDate, subjectId,
						status);
			} else {
				logger.warn("Yêu cầu không hợp lệ: Thiếu tham số 'week' hoặc 'startDate' và 'endDate'");
				return ResponseEntity.badRequest().body(null);
			}
			logger.info("Lấy thành công {} lịch cho sinh viên studentId={}", schedules.size(), studentId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch cho sinh viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/teachers/{teacherId}/schedules")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getTeacherSchedule(@PathVariable Long teacherId,
			@RequestParam Long semesterId, @RequestParam(required = false) Integer week,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Long subjectId,
			@RequestParam(required = false, defaultValue = "CONFIRMED") String status) {
		logger.info("Lấy lịch cho giảng viên teacherId={} và semesterId={} với week={} hoặc từ {} đến {}", teacherId,
				semesterId, week, startDate, endDate);
		try {
			List<ScheduleResponse> schedules;
			if (week != null) {
				schedules = schedulingService.getTeacherSchedule(teacherId, semesterId, week, subjectId, status);
			} else if (startDate != null && endDate != null) {
				schedules = schedulingService.getTeacherScheduleByWeek(teacherId, startDate, endDate, subjectId,
						status);
			} else {
				schedules = schedulingService.getTeacherSchedule(teacherId, semesterId, subjectId, status);
			}
			logger.info("Lấy thành công {} lịch cho giảng viên teacherId={}", schedules.size(), teacherId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch cho giảng viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/schedules/class/{classId}/combined")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getCombinedScheduleByClass(@PathVariable Long classId,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
			@RequestParam(required = false) Integer week,
			@RequestParam(required = false, defaultValue = "CONFIRMED") String status) {
		logger.info("Lấy lịch tổng hợp cho lớp classId={} với week={} hoặc từ {} đến {}", classId, week, startDate,
				endDate);
		try {
			List<ScheduleResponse> schedules;
			if (week != null) {
				schedules = schedulingService.getCombinedScheduleByClassAndWeek(classId, week, status);
			} else if (startDate != null && endDate != null) {
				schedules = schedulingService.getCombinedScheduleByClass(classId, startDate, endDate, status);
			} else {
				schedules = schedulingService.getScheduleByClass(classId, status);
			}
			logger.info("Lấy thành công {} lịch cho lớp classId={}", schedules.size(), classId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch tổng hợp cho lớp: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/students/{studentId}/schedules/day")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getStudentScheduleByDay(@PathVariable Long studentId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		logger.info("Lấy lịch theo ngày cho sinh viên studentId={} và ngày={}", studentId, date);
		try {
			List<ScheduleResponse> schedules = schedulingService.getStudentScheduleByDay(studentId, date);
			logger.info("Lấy thành công {} lịch cho sinh viên studentId={}", schedules.size(), studentId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo ngày cho sinh viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/students/{studentId}/schedules/week")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getStudentScheduleByWeek(@PathVariable Long studentId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		logger.info("Lấy lịch theo tuần cho sinh viên studentId={} từ {} đến {}", studentId, startDate, endDate);
		try {
			List<ScheduleResponse> schedules = schedulingService.getStudentScheduleByWeek(studentId, startDate,
					endDate);
			logger.info("Lấy thành công {} lịch cho sinh viên studentId={}", schedules.size(), studentId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo tuần cho sinh viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/teachers/{teacherId}/schedules/day")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getTeacherScheduleByDay(@PathVariable Long teacherId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		logger.info("Lấy lịch theo ngày cho giảng viên teacherId={} và ngày={}", teacherId, date);
		try {
			List<ScheduleResponse> schedules = schedulingService.getTeacherScheduleByDay(teacherId, date);
			logger.info("Lấy thành công {} lịch cho giảng viên teacherId={}", schedules.size(), teacherId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo ngày cho giảng viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}

	@GetMapping("/teachers/{teacherId}/schedules/week")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getTeacherScheduleByWeek(@PathVariable Long teacherId,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		logger.info("Lấy lịch theo tuần cho giảng viên teacherId={} từ {} đến {}", teacherId, startDate, endDate);
		try {
			List<ScheduleResponse> schedules = schedulingService.getTeacherScheduleByWeek(teacherId, startDate,
					endDate);
			logger.info("Lấy thành công {} lịch cho giảng viên teacherId={}", schedules.size(), teacherId);
			return ResponseEntity.ok(schedules);
		} catch (Exception e) {
			logger.error("Lỗi khi lấy lịch theo tuần cho giảng viên: ", e);
			return ResponseEntity.status(500).body(null);
		}
	}
}