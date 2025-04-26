package com.studentApp.service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.entity.Class;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Curriculum;
import com.studentApp.entity.Department;
import com.studentApp.entity.Major;
import com.studentApp.entity.Semester;
import com.studentApp.entity.Subject;
import com.studentApp.entity.Teacher;
import com.studentApp.entity.Teacher.Gender;
import com.studentApp.entity.TimeWindow;
import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.CurriculumRepository;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.MajorRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.SubjectRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.TimeWindowRepository;

@Service
public class ImportService {

	@Autowired
	private CurriculumRepository curriculumRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private MajorRepository majorRepository;

	@Autowired
	private SemesterRepository semesterRepository;

	@Autowired
	private ClassGroupRepository classGroupRepository;

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private SubjectRepository subjectRepository;

	@Autowired
	private ClassRepository classRepository;

	@Autowired
	private TimeWindowRepository timeWindowRepository;

	@Transactional
	public void importCurriculums(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Curriculum> curriculums = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Curriculum curriculum = new Curriculum();
				curriculum.setCurriculumCode(record.get("curriculum_code"));
				curriculum.setCurriculumName(record.get("curriculum_name"));
				curriculum.setDescription(record.get("description"));
				curriculums.add(curriculum);
			}
			curriculumRepository.saveAll(curriculums);
		}
	}

	@Transactional
	public void importDepartments(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Department> departments = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Department department = new Department();
				department.setDeptCode(record.get("dept_code"));
				department.setDeptName(record.get("dept_name"));
				department.setDescription(record.get("description"));
				departments.add(department);
			}
			departmentRepository.saveAll(departments);
		}
	}

	@Transactional
	public void importMajors(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Major> majors = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Major major = new Major();
				major.setMajorCode(record.get("major_code"));
				major.setMajorName(record.get("major_name"));
				major.setDeptId(Long.parseLong(record.get("dept_id")));
				Curriculum curriculum = curriculumRepository.findById(Long.parseLong(record.get("curriculum_id")))
						.orElseThrow(() -> new RuntimeException("Curriculum not found"));
				major.setCurriculum(curriculum);
				major.setDescription(record.get("description"));
				majors.add(major);
			}
			majorRepository.saveAll(majors);
		}
	}

	@Transactional
	public void importSemesters(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Semester> semesters = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Semester semester = new Semester();
				semester.setStartDate(LocalDate.parse(record.get("start_date")));
				semester.setEndDate(LocalDate.parse(record.get("end_date")));
				semesters.add(semester);
			}
			semesterRepository.saveAll(semesters);
		}
	}

	@Transactional
	public void importClassGroups(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<ClassGroup> classGroups = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				ClassGroup classGroup = new ClassGroup();
				classGroup.setGroupCode(record.get("group_code"));
				classGroup.setGroupName(record.get("group_name"));
				Major major = majorRepository.findById(Long.parseLong(record.get("major_id")))
						.orElseThrow(() -> new RuntimeException("Major not found"));
				classGroup.setMajor(major);
				classGroup.setShift(record.get("shift"));
				Semester semester = semesterRepository.findById(Long.parseLong(record.get("semester_id")))
						.orElseThrow(() -> new RuntimeException("Semester not found"));
				classGroup.setSemester(semester);
				classGroups.add(classGroup);
			}
			classGroupRepository.saveAll(classGroups);
		}
	}

	@Transactional
	public void importTeachers(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Teacher> teachers = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Teacher teacher = new Teacher();
				teacher.setTeacherCode(record.get("teacher_code"));
				teacher.setTeacherName(record.get("teacher_name"));
				teacher.setTeacherDateOfBirth(Date.valueOf(record.get("date_of_birth")));
				teacher.setTeacherGender(Gender.valueOf(record.get("gender")));
				teacher.setTeacherAddress(record.get("address"));
				teacher.setTeacherPhoneNumber(record.get("phone_number"));
				teacher.setTeacherEmail(record.get("email"));
				Department department = departmentRepository.findById(Long.parseLong(record.get("dept_id")))
						.orElseThrow(() -> new RuntimeException("Department not found"));
				teacher.setDepartment(department);
				teachers.add(teacher);
			}
			teacherRepository.saveAll(teachers);
		}
	}

	@Transactional
	public void importSubjects(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Subject> subjects = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Subject subject = new Subject();
				subject.setSubjectCode(record.get("subject_code"));
				subject.setSubjectName(record.get("subject_name"));
				subject.setCredits(Integer.parseInt(record.get("credits")));
				Semester semester = semesterRepository.findById(Long.parseLong(record.get("semester_id")))
						.orElseThrow(() -> new RuntimeException("Semester not found"));
				subject.setSemester(semester);
				subject.setDeptId(Long.parseLong(record.get("dept_id")));
				subject.setDescription(record.get("description"));
				subjects.add(subject);
			}
			subjectRepository.saveAll(subjects);
		}
	}

	@Transactional
	public void importClasses(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<Class> classes = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				Class classEntity = new Class();
				classEntity.setClassCode(record.get("class_code"));
				classEntity.setClassName(record.get("class_name"));
				Subject subject = subjectRepository.findById(Long.parseLong(record.get("subject_id")))
						.orElseThrow(() -> new RuntimeException("Subject not found"));
				classEntity.setSubject(subject);
				Teacher teacher = teacherRepository.findById(Long.parseLong(record.get("teacher_id")))
						.orElseThrow(() -> new RuntimeException("Teacher not found"));
				classEntity.setTeacher(teacher);
				ClassGroup classGroup = classGroupRepository.findById(Long.parseLong(record.get("class_group_id")))
						.orElseThrow(() -> new RuntimeException("ClassGroup not found"));
				classEntity.setClassGroup(classGroup);
				classEntity.setStartDate(LocalDate.parse(record.get("start_date")));
				classEntity.setEndDate(LocalDate.parse(record.get("end_date")));
				classEntity.setClassroom(record.get("classroom"));
				classEntity.setShift(record.get("shift"));
				classEntity.setPriority(Integer.parseInt(record.get("priority")));
				classes.add(classEntity);
			}
			classRepository.saveAll(classes);
		}
	}

	@Transactional
	public void importTimeWindows(InputStream inputStream) throws Exception {
		try (CSVParser csvParser = new CSVParser(new InputStreamReader(inputStream),
				CSVFormat.DEFAULT.withFirstRecordAsHeader())) {
			List<TimeWindow> timeWindows = new ArrayList<>();
			for (CSVRecord record : csvParser) {
				TimeWindow timeWindow = new TimeWindow();
				timeWindow.setIdClass(Integer.parseInt(record.get("id_class")));
				timeWindow.setDayOfWeek(record.get("day_of_week"));
				timeWindow.setSlot(Integer.parseInt(record.get("slot")));
				timeWindow.setCreatedDate(LocalDate.parse(record.get("created_date")));
				Class classEntity = classRepository.findById(Long.parseLong(record.get("id_class")))
						.orElseThrow(() -> new RuntimeException("Class not found"));
				timeWindow.setClazz(classEntity);
				timeWindows.add(timeWindow);
			}
			timeWindowRepository.saveAll(timeWindows);
		}
	}
}