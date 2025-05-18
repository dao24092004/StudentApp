package com.studentApp.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.studentApp.dto.request.TeacherSubjectRegistrationRequest;
import com.studentApp.dto.response.ScheduleResponse;
import com.studentApp.dto.response.SubjectResponse;
import com.studentApp.entity.Schedule;
import com.studentApp.entity.Semester;
import com.studentApp.entity.Subject;
import com.studentApp.entity.Teacher;
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

    @Autowired
    private SemesterRepository semesterRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private TimeWindowRepository timeWindowRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private TeacherSubjectRegistrationRepository teacherSubjectRegistrationRepository;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Autowired
    private SubjectMapper subjectMapper;

    public void callPythonScript(String semesterId) throws IOException, InterruptedException {
        String pythonPath = "C:\\Users\\phamm\\AppData\\Local\\Microsoft\\WindowsApps\\PythonSoftwareFoundation.Python.3.11_qbz5n2kfra8p0\\python.exe";
        String scriptPath = "D:\\workspace\\DoAn\\Java\\TruongHoc\\src\\main\\resources\\scripts\\schedule.py";

        ProcessBuilder pb = new ProcessBuilder(pythonPath, scriptPath, "--semesterId", semesterId);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException(
                    "Python script failed with exit code: " + exitCode + "\nOutput: " + output.toString());
        }
    }

    public void generateSchedule(String semesterId) throws IOException, InterruptedException {
        callPythonScript(semesterId);
    }

    public void registerSubjects(TeacherSubjectRegistrationRequest request) {
        Long teacherId = request.getTeacherId();
        Long semesterId = request.getSemesterId();
        List<Long> subjectIds = request.getSubjectIds();

        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        for (Long subjectId : subjectIds) {
            Subject subject = subjectRepository.findById(subjectId)
                    .orElseThrow(() -> new RuntimeException("Subject not found"));

            TeacherSubjectRegistration registration = new TeacherSubjectRegistration();
            registration.setTeacher(teacher);
            registration.setSubject(subject);
            registration.setSemester(semester);
            teacherSubjectRegistrationRepository.save(registration);
        }
    }

    public void assignClasses(Long semesterId) {
        Semester semester = semesterRepository.findById(semesterId)
                .orElseThrow(() -> new RuntimeException("Semester not found"));

        List<TeacherSubjectRegistration> registrations = teacherSubjectRegistrationRepository
                .findBySemesterId(semesterId);
        List<com.studentApp.entity.Class> classes = classRepository.findUnassignedBySemesterId(semesterId);

        for (TeacherSubjectRegistration registration : registrations) {
            Teacher teacher = registration.getTeacher();
            Subject subject = registration.getSubject();

            for (com.studentApp.entity.Class classEntity : classes) {
                if (classEntity.getSubject().getId().equals(subject.getId())) {
                    classEntity.setTeacher(teacher);
                    classRepository.save(classEntity);
                    classes.remove(classEntity);
                    break;
                }
            }
        }
    }

    public List<SubjectResponse> getDepartmentSubjects(Long semesterId, Long deptId) {
        return subjectRepository.findBySemesterIdAndDeptId(semesterId, deptId).stream().map(subjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<SubjectResponse> getTeacherSubjects(Long teacherId, Long semesterId) {
        return subjectRepository.findByTeacherIdAndSemesterId(teacherId, semesterId).stream().map(subjectMapper::toDTO)
                .collect(Collectors.toList());
    }

    public ScheduleResponse addSchedule(ScheduleResponse scheduleDTO) {
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
    }

    public ScheduleResponse updateSchedule(Long id, ScheduleResponse scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        scheduleMapper.updateEntity(schedule, scheduleDTO);
        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDTO(schedule);
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
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT_VIEW"))) {
            Long studentId = Long.parseLong(username);
            schedules = scheduleRepository.findByStudentIdAndWeek(studentId, startDate, endDate);
        } else {
            schedules = scheduleRepository.findByWeek(startDate, endDate);
        }

        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getScheduleByDay(LocalDate date) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        List<Schedule> schedules;

        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("TEACHER_VIEW"))) {
            Long teacherId = Long.parseLong(username);
            schedules = scheduleRepository.findByTeacherIdAndDate(teacherId, date);
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("STUDENT_VIEW"))) {
            Long studentId = Long.parseLong(username);
            schedules = scheduleRepository.findByStudentIdAndDate(studentId, date);
        } else {
            schedules = scheduleRepository.findByDate(date);
        }

        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getScheduleByClass(Long classId) {
        return scheduleRepository.findByClassId(classId).stream().map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleResponse> getScheduleByClass(Long classId, String status) {
        List<Schedule> schedules = scheduleRepository.findByClassId(classId);
        return filterSchedules(schedules, null, status);
    }

    public List<ScheduleResponse> getStudentSchedule(Long studentId, Long semesterId, Integer week, Long subjectId,
            String status) {
        if (week == null) {
            throw new IllegalArgumentException("Parameter 'week' is required when fetching student schedule by week");
        }
        List<Schedule> schedules = scheduleRepository.findByStudentIdAndSemesterIdAndWeek(studentId, semesterId, week);
        return filterSchedules(schedules, subjectId, status);
    }

    public List<ScheduleResponse> getTeacherSchedule(Long teacherId, Long semesterId, Long subjectId, String status) {
        List<Schedule> schedules = scheduleRepository.findByTeacherIdAndSemesterId(teacherId, semesterId);
        return filterSchedules(schedules, subjectId, status);
    }

    public List<ScheduleResponse> getTeacherSchedule(Long teacherId, Long semesterId, Integer week, Long subjectId,
            String status) {
        if (week == null) {
            throw new IllegalArgumentException("Parameter 'week' is required when fetching teacher schedule by week");
        }
        List<Schedule> schedules = scheduleRepository.findByTeacherIdAndSemesterIdAndWeek(teacherId, semesterId, week);
        return filterSchedules(schedules, subjectId, status);
    }

    public List<ScheduleResponse> getStudentScheduleByDay(Long studentId, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByStudentIdAndDate(studentId, date);
        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getStudentScheduleByWeek(Long studentId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByStudentIdAndWeek(studentId, startDate, endDate);
        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getStudentScheduleByWeek(Long studentId, LocalDate startDate, LocalDate endDate,
            Long subjectId, String status) {
        List<Schedule> schedules = scheduleRepository.findByStudentIdAndWeek(studentId, startDate, endDate);
        return filterSchedules(schedules, subjectId, status);
    }

    public List<ScheduleResponse> getTeacherScheduleByDay(Long teacherId, LocalDate date) {
        List<Schedule> schedules = scheduleRepository.findByTeacherIdAndDate(teacherId, date);
        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getTeacherScheduleByWeek(Long teacherId, LocalDate startDate, LocalDate endDate) {
        List<Schedule> schedules = scheduleRepository.findByTeacherIdAndWeek(teacherId, startDate, endDate);
        return schedules.stream().map(scheduleMapper::toDTO).collect(Collectors.toList());
    }

    public List<ScheduleResponse> getTeacherScheduleByWeek(Long teacherId, LocalDate startDate, LocalDate endDate,
            Long subjectId, String status) {
        List<Schedule> schedules = scheduleRepository.findByTeacherIdAndWeek(teacherId, startDate, endDate);
        return filterSchedules(schedules, subjectId, status);
    }

    public List<ScheduleResponse> getCombinedScheduleByClass(Long classId, LocalDate startDate, LocalDate endDate,
            String status) {
        List<Schedule> schedules = scheduleRepository.findByClassIdAndWeek(classId, startDate, endDate);
        return filterSchedules(schedules, null, status);
    }

    public List<ScheduleResponse> getCombinedScheduleByClassAndWeek(Long classId, Integer week, String status) {
        if (week == null) {
            throw new IllegalArgumentException("Parameter 'week' is required when fetching class schedule by week");
        }
        List<Schedule> schedules = scheduleRepository.findByClassIdAndWeekNumber(classId, week);
        return filterSchedules(schedules, null, status);
    }

    private List<ScheduleResponse> filterSchedules(List<Schedule> schedules, Long subjectId, String status) {
        return schedules.stream()
                .filter(s -> subjectId == null || (s.getSubject() != null && s.getSubject().getId().equals(subjectId)))
                .filter(s -> status == null || (s.getStatus() != null && s.getStatus().equalsIgnoreCase(status)))
                .map(scheduleMapper::toDTO)
                .collect(Collectors.toList());
    }
}