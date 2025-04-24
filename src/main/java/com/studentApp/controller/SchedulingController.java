package com.studentApp.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

	private final SchedulingService schedulingService;

	public SchedulingController(SchedulingService schedulingService) {
		this.schedulingService = schedulingService;
	}

	@PostMapping("/teachers/register-subjects")
	@PreAuthorize("hasAuthority('TEACHER_REGISTER')")
	public ResponseEntity<Map<String, String>> registerSubjects(
			@RequestBody TeacherSubjectRegistrationRequest request) {
		schedulingService.registerSubjects(request);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@PostMapping("/assign-classes")
	@PreAuthorize("hasAuthority('CLASS_ASSIGN')")
	public ResponseEntity<Map<String, String>> assignClasses(@RequestBody AssignClassesRequest request) {
		schedulingService.assignClasses(request.getSemesterId());
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@GetMapping("/department-subjects")
	@PreAuthorize("hasAnyAuthority('TEACHER_VIEW', 'CLASS_VIEW')")
	public ResponseEntity<List<SubjectResponse>> getDepartmentSubjects(@RequestParam Long semesterId,
			@RequestParam Long deptId) {
		return ResponseEntity.ok(schedulingService.getDepartmentSubjects(semesterId, deptId));
	}

	@GetMapping("/teachers/{teacherId}/subjects")
	@PreAuthorize("hasAuthority('TEACHER_VIEW')")
	public ResponseEntity<List<SubjectResponse>> getTeacherSubjects(@PathVariable Long teacherId,
			@RequestParam Long semesterId) {
		return ResponseEntity.ok(schedulingService.getTeacherSubjects(teacherId, semesterId));
	}

	@PostMapping("/schedules/generate")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<Map<String, String>> generateSchedule(@RequestBody ScheduleRequest request) throws Exception {
		schedulingService.generateSchedule(request.getSemesterId());
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@PostMapping("/create/schedules")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<ScheduleResponse> addSchedule(@RequestBody ScheduleResponse scheduleResponse) {
		ScheduleResponse createdSchedule = schedulingService.addSchedule(scheduleResponse);
		return ResponseEntity.ok(createdSchedule);
	}

	@PutMapping("/update/schedules/{id}")
	@PreAuthorize("hasAuthority('CLASS_UPDATE')")
	public ResponseEntity<ScheduleResponse> updateSchedule(@PathVariable Long id,
			@RequestBody ScheduleResponse scheduleResponse) {
		ScheduleResponse updatedSchedule = schedulingService.updateSchedule(id, scheduleResponse);
		return ResponseEntity.ok(updatedSchedule);
	}

	@DeleteMapping("/delete/schedules/{id}")
	@PreAuthorize("hasAuthority('CLASS_DELETE')")
	public ResponseEntity<Map<String, String>> deleteSchedule(@PathVariable Long id) {
		schedulingService.deleteSchedule(id);
		return ResponseEntity.ok(Map.of("status", "success"));
	}

	@GetMapping("/schedules/week")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByWeek(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
		return ResponseEntity.ok(schedulingService.getScheduleByWeek(startDate, endDate));
	}

	@GetMapping("/schedules/day")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByDay(
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return ResponseEntity.ok(schedulingService.getScheduleByDay(date));
	}

	@GetMapping("/schedules/class/{classId}")
	@PreAuthorize("hasAuthority('CLASS_VIEW')")
	public ResponseEntity<List<ScheduleResponse>> getScheduleByClass(@PathVariable Long classId) {
		return ResponseEntity.ok(schedulingService.getScheduleByClass(classId));
	}
}