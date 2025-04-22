package com.studentApp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.studentApp.dto.request.TeacherSubjectRegistrationRequest;
import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.entity.Schedule;
import com.studentApp.entity.TeacherSubjectRegistration;
import com.studentApp.mapper.ScheduleMapper;
import com.studentApp.mapper.SubjectMapper;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.ScheduleRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.SubjectRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.TeacherSubjectRegistrationRepository;
import com.studentApp.repository.TimeWindowRepository;

@Service
public class SchedulingService {

	@Value("${python.script.path}")
	private String pythonScriptPath;

	private final SemesterRepository semesterRepository;
	private final TeacherRepository teacherRepository;
	private final SubjectRepository subjectRepository;
	private final ClassRepository classRepository;
	private final TimeWindowRepository timeWindowRepository;
	private final ScheduleRepository scheduleRepository;
	private final TeacherSubjectRegistrationRepository teacherSubjectRegistrationRepository;
	private final ScheduleMapper scheduleMapper;

	public SchedulingService(SemesterRepository semesterRepository, TeacherRepository teacherRepository,
			SubjectRepository subjectRepository, ClassRepository classRepository,
			TimeWindowRepository timeWindowRepository, ScheduleRepository scheduleRepository,
			TeacherSubjectRegistrationRepository teacherSubjectRegistrationRepository, ScheduleMapper scheduleMapper) {
		this.semesterRepository = semesterRepository;
		this.teacherRepository = teacherRepository;
		this.subjectRepository = subjectRepository;
		this.classRepository = classRepository;
		this.timeWindowRepository = timeWindowRepository;
		this.scheduleRepository = scheduleRepository;
		this.teacherSubjectRegistrationRepository = teacherSubjectRegistrationRepository;
		this.scheduleMapper = scheduleMapper;
	}

	private String callPythonScript(String action, String... args) throws Exception {
		String[] command = new String[3 + args.length];
		command[0] = "python3";
		command[1] = pythonScriptPath;
		command[2] = action;
		for (int i = 0; i < args.length; i++) {
			command[i + 3] = args[i];
		}

		ProcessBuilder pb = new ProcessBuilder(command);
		pb.redirectErrorStream(true);
		Process process = pb.start();

		StringBuilder output = new StringBuilder();
		try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				output.append(line).append("\n");
				System.out.println("Python Output: " + line);
			}
		}

		int exitCode = process.waitFor();
		if (exitCode != 0) {
			throw new RuntimeException("Python script failed with exit code: " + exitCode + "\nOutput: " + output);
		}
		return output.toString();
	}

	public void registerSubjects(TeacherSubjectRegistrationRequest request) {
		Long teacherId = request.getTeacherId();
		Long semesterId = request.getSemesterId();
		List<Long> subjectIds = request.getSubjectIds();

		for (Long subjectId : subjectIds) {
			TeacherSubjectRegistration registration = new TeacherSubjectRegistration();
			registration.setTeacherId(teacherId);
			registration.setSubjectId(subjectId);
			registration.setSemesterId(semesterId);
			teacherSubjectRegistrationRepository.save(registration);
		}
	}

	public void assignClasses(Long semesterId) {
		List<TeacherSubjectRegistration> registrations = teacherSubjectRegistrationRepository
				.findBySemesterId(semesterId);
		List<com.studentApp.entity.Class> classes = classRepository.findBySemesterIdAndTeacherIdIsNull(semesterId);

		for (TeacherSubjectRegistration registration : registrations) {
			Long teacherId = registration.getTeacherId();
			Long subjectId = registration.getSubjectId();

			for (com.studentApp.entity.Class classEntity : classes) {
				if (classEntity.getSubjectId().equals(subjectId)) {
					classEntity.setTeacherId(teacherId);
					classRepository.save(classEntity);
					classes.remove(classEntity);
					break;
				}
			}
		}
	}

	public List<SubjectResponse> getDepartmentSubjects(Long semesterId, Long deptId) {
		return subjectRepository.findBySemesterIdAndDeptId(semesterId, deptId).stream().map(SubjectMapper::toDTO)
				.collect(Collectors.toList());
	}

	public List<SubjectResponse> getTeacherSubjects(Long teacherId, Long semesterId) {
		return subjectRepository.findByTeacherIdAndSemesterId(teacherId, semesterId).stream().map(SubjectMapper::toDTO)
				.collect(Collectors.toList());
	}

	public void generateSchedule(Long semesterId) throws Exception {
		callPythonScript("generate", semesterId.toString());
	}

	public ScheduleResponse addSchedule(ScheduleResponse scheduleDTO) {
		Schedule schedule = ScheduleMapper.toEntity(scheduleDTO);
		schedule = scheduleRepository.save(schedule);
		return ScheduleMapper.toDTO(schedule);
	}

	public ScheduleResponse updateSchedule(Long id, ScheduleResponse scheduleDTO) {
		Schedule schedule = scheduleRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Schedule not found"));
		ScheduleMapper.updateEntity(schedule, scheduleDTO);
		schedule = scheduleRepository.save(schedule);
		return ScheduleMapper.toDTO(schedule);
	}

	public void deleteSchedule(Long id) {
		scheduleRepository.deleteById(id);
	}

	public List<ScheduleResponse> getScheduleByWeek(LocalDate startDate, LocalDate endDate) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		List<Schedule> schedules;

		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEACHER_VIEW"))) {
			Long teacherId = Long.parseLong(username);
			schedules = scheduleRepository.findByTeacherIdAndWeek(teacherId, startDate, endDate);
		} else {
			schedules = scheduleRepository.findByWeek(startDate, endDate);
		}

		return schedules.stream().map(ScheduleMapper::toDTO).collect(Collectors.toList());
	}

	public List<ScheduleResponse> getScheduleByDay(LocalDate date) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		List<Schedule> schedules;

		if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEACHER_VIEW"))) {
			Long teacherId = Long.parseLong(username);
			schedules = scheduleRepository.findByTeacherIdAndDate(teacherId, date);
		} else {
			schedules = scheduleRepository.findByDate(date);
		}

		return schedules.stream().map(ScheduleMapper::toDTO).collect(Collectors.toList());
	}

	public List<ScheduleResponse> getScheduleByClass(Long classId) {
		return scheduleRepository.findByClassId(classId).stream().map(ScheduleMapper::toDTO)
				.collect(Collectors.toList());
	}
}