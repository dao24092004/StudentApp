package com.studentApp.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.entity.Class;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Curriculum;
import com.studentApp.entity.Department;
import com.studentApp.entity.Major;
import com.studentApp.entity.Permission;
import com.studentApp.entity.Role;
import com.studentApp.entity.Semester;
import com.studentApp.entity.Subject;
import com.studentApp.entity.Teacher;
import com.studentApp.entity.Teacher.Gender;
import com.studentApp.entity.TimeWindow;
import com.studentApp.entity.User;
import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.ClassRepository;
import com.studentApp.repository.CurriculumRepository;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.MajorRepository;
import com.studentApp.repository.PermissionRepository;
import com.studentApp.repository.RegistrationRepository;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.SemesterRepository;
import com.studentApp.repository.SubjectRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.TimeWindowRepository;
import com.studentApp.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class DataGeneratorService {

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

	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@PersistenceContext
	private EntityManager entityManager; // Thêm EntityManager để thực hiện lệnh SQL

	private final Random random = new Random();
	private static final Logger logger = LoggerFactory.getLogger(DataGeneratorService.class);

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final List<String> firstNames = Arrays.asList("Nguyễn", "Trần", "Lê", "Phạm", "Hoàng", "Huỳnh", "Phan",
			"Vũ", "Đặng", "Bùi", "Đỗ", "Hồ", "Ngô", "Dương", "Lý", "Trịnh", "Đinh", "Lương", "Mai", "Tô");
	private final List<String> lastNames = Arrays.asList("Anh", "Bình", "Cường", "Dung", "Duy", "Hà", "Hải", "Hạnh",
			"Hòa", "Hùng", "Khánh", "Lan", "Linh", "Long", "Minh", "Nam", "Ngọc", "Nhi", "Phúc", "Thảo", "Thành",
			"Thịnh", "Thu", "Thư", "Tín", "Tùng", "Vân", "Việt", "Vy", "Xuân");

	private final List<String> addresses = Arrays.asList("123 Nguyễn Trãi, Quận 5, TP.HCM", "45 Lê Lợi, Quận 1, TP.HCM",
			"78 Trần Phú, Nha Trang, Khánh Hòa", "12 Nguyễn Huệ, Huế, Thừa Thiên Huế", "56 Phạm Văn Đồng, Đà Nẵng",
			"89 Nguyễn Văn Cừ, Hà Nội", "34 Lý Thường Kiệt, Cần Thơ", "67 Nguyễn Thị Minh Khai, Vũng Tàu",
			"90 Lê Đại Hành, Hải Phòng", "15 Điện Biên Phủ, Đà Lạt, Lâm Đồng");

	private final List<String> classrooms = Arrays.asList("A101", "A102", "A201", "A202", "B101", "B102", "B201",
			"B202", "C101", "C102", "C201", "C202", "D101", "D102", "D201", "D202", "E101", "E102", "E201", "E202");

	private final List<String> shifts = Arrays.asList("MORNING", "AFTERNOON", "EVENING");

	private final List<String> daysOfWeek = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

	@Transactional
	public void clearData() {
		logger.info("Clearing all data from database...");
		try {
			// Sử dụng TRUNCATE ... CASCADE để xóa tất cả dữ liệu và các bảng liên quan
			entityManager
					.createNativeQuery("TRUNCATE TABLE tbl_grade_history, tbl_grade_appeal, tbl_grade, tbl_attendance, "
							+ "tbl_teacher_subject_registration, tbl_registration, tbl_time_window, tbl_class, "
							+ "tbl_subject, tbl_teacher, tbl_class_group, tbl_semester, tbl_major, tbl_department, "
							+ "tbl_curriculum, tbl_role_permission, tbl_permission, tbl_role, tbl_user, tbl_student "
							+ "RESTART IDENTITY CASCADE")
					.executeUpdate();

			logger.info("All data cleared successfully.");
		} catch (Exception e) {
			logger.error("Error clearing data: {}", e.getMessage());
			throw new RuntimeException("Error clearing data: " + e.getMessage());
		}
	}

	@Transactional
	public void generateSampleData() {
		logger.info("Starting to generate sample data...");

		// 0. Sinh dữ liệu cho Permission (các quyền)
		List<Permission> permissions = new ArrayList<>();
		if (permissionRepository.findAll().isEmpty()) {
			logger.info("Generating Permission data...");
			String[] permissionNames = { "DATA_GENERATE", "CURRICULUM_TEMPLATE", "CLASS_CREATE", "SUBJECT_VIEW",
					"TEACHER_MANAGE", "DEPARTMENT_EDIT", "MAJOR_CREATE", "SEMESTER_VIEW" };
			for (String permissionName : permissionNames) {
				Permission permission = new Permission();
				permission.setPermissionName(permissionName);
				permission.setDescription("Permission for " + permissionName.toLowerCase().replace("_", " "));
				permissions.add(permission);
			}
			permissionRepository.saveAll(permissions);
			logger.info("Permission data generated successfully.");
		} else {
			logger.warn("Database already contains Permission data. Using existing data.");
			permissions = permissionRepository.findAll();
		}

		// 1. Sinh dữ liệu cho Role (vai trò)
		List<Role> roles = new ArrayList<>();
		if (roleRepository.findAll().isEmpty()) {
			logger.info("Generating Role data...");
			Role adminRole = new Role();
			adminRole.setRoleName("ROLE_ADMIN");
			adminRole.setDescription("Administrator role with full access");
			adminRole.setPermissions(new ArrayList<>(permissions));
			adminRole.setCreatedAt(LocalDateTime.now());
			adminRole.setUpdatedAt(LocalDateTime.now());
			roles.add(adminRole);

			Role userRole = new Role();
			userRole.setRoleName("ROLE_USER");
			userRole.setDescription("Regular user role with limited access");
			userRole.setPermissions(new ArrayList<>(permissions.subList(0, 2)));
			userRole.setCreatedAt(LocalDateTime.now());
			userRole.setUpdatedAt(LocalDateTime.now());
			roles.add(userRole);

			roleRepository.saveAll(roles);
			logger.info("Role data generated successfully.");
		} else {
			logger.warn("Database already contains Role data. Using existing data.");
			roles = roleRepository.findAll();
		}

		// 2. Sinh dữ liệu cho User (tài khoản)
		List<User> users = new ArrayList<>();
		if (userRepository.findAll().isEmpty()) {
			logger.info("Generating User data...");
			User adminUser = new User();
			adminUser.setUsername("admin1");
			adminUser.setPassword(passwordEncoder.encode("admin123"));
			adminUser.setEmail("admin1@university.edu.vn");
			adminUser.setCreatedAt(LocalDateTime.now());
			adminUser.setUpdatedAt(LocalDateTime.now());
			adminUser.setRole(roles.get(0));
			users.add(adminUser);

			User regularUser = new User();
			regularUser.setUsername("user1");
			regularUser.setPassword(passwordEncoder.encode("user123"));
			regularUser.setEmail("user1@university.edu.vn");
			regularUser.setCreatedAt(LocalDateTime.now());
			regularUser.setUpdatedAt(LocalDateTime.now());
			regularUser.setRole(roles.get(1));
			users.add(regularUser);

			userRepository.saveAll(users);
			logger.info("User data generated successfully.");
		} else {
			logger.warn("Database already contains User data. Using existing data.");
			users = userRepository.findAll();
		}

		// 3. Sinh dữ liệu cho Curriculum (5 mẫu)
		List<Curriculum> curriculums = new ArrayList<>();
		if (curriculumRepository.findAll().isEmpty()) {
			logger.info("Generating Curriculum data...");
			for (int i = 1; i <= 5; i++) {
				Curriculum curriculum = new Curriculum();
				curriculum.setCurriculumCode(String.format("CURR%03d", i));
				curriculum.setCurriculumName("Chương trình đào tạo " + (i == 1 ? "Công nghệ thông tin"
						: i == 2 ? "Kinh tế" : i == 3 ? "Kỹ thuật" : i == 4 ? "Y học" : "Giáo dục"));
				curriculum.setDescription("Chương trình đào tạo " + curriculum.getCurriculumName());
				curriculum.setCreatedAt(LocalDateTime.now());
				curriculum.setUpdatedAt(LocalDateTime.now());
				curriculums.add(curriculum);
			}
			curriculumRepository.saveAll(curriculums);
			logger.info("Curriculum data generated successfully.");
		} else {
			logger.warn("Database already contains Curriculum data. Using existing data.");
			curriculums = curriculumRepository.findAll();
		}

		// 4. Sinh dữ liệu cho Department (6 mẫu)
		List<Department> departments = new ArrayList<>();
		if (departmentRepository.findAll().isEmpty()) {
			logger.info("Generating Department data...");
			for (int i = 1; i <= 6; i++) {
				Department department = new Department();
				department.setDeptCode(String.format("KHOA%03d", i));
				department.setDeptName(i == 1 ? "Khoa Công nghệ thông tin"
						: i == 2 ? "Khoa Kinh tế"
								: i == 3 ? "Khoa Cơ khí"
										: i == 4 ? "Khoa Y" : i == 5 ? "Khoa Sư phạm" : "Khoa Khoa học tự nhiên");
				department.setDescription("Khoa " + department.getDeptName());
				department.setCreatedAt(LocalDateTime.now());
				department.setUpdatedAt(LocalDateTime.now());
				departments.add(department);
			}
			departmentRepository.saveAll(departments);
			logger.info("Department data generated successfully.");
		} else {
			logger.warn("Database already contains Department data. Using existing data.");
			departments = departmentRepository.findAll();
		}

		// 5. Sinh dữ liệu cho Major (12 mẫu)
		List<Major> majors = new ArrayList<>();
		if (majorRepository.findAll().isEmpty()) {
			logger.info("Generating Major data...");
			for (int i = 1; i <= 12; i++) {
				Major major = new Major();
				major.setMajorCode(String.format("MAJ%03d", i));
				major.setMajorName(i <= 3 ? "Kỹ thuật phần mềm"
						: i <= 5 ? "Kinh tế quốc tế"
								: i <= 7 ? "Kỹ thuật ô tô"
										: i <= 9 ? "Y đa khoa" : i <= 11 ? "Sư phạm Toán" : "Hóa học");
				major.setDeptId(departments.get((i - 1) % departments.size()).getId());
				major.setCurriculum(curriculums.get((i - 1) / 3));
				major.setDescription("Chuyên ngành " + major.getMajorName());
				major.setCreatedAt(LocalDateTime.now());
				major.setUpdatedAt(LocalDateTime.now());
				majors.add(major);
			}
			majorRepository.saveAll(majors);
			logger.info("Major data generated successfully.");
		} else {
			logger.warn("Database already contains Major data. Using existing data.");
			majors = majorRepository.findAll();
		}

		// 6. Sinh dữ liệu cho Semester (3 mẫu)
		List<Semester> semesters = new ArrayList<>();
		if (semesterRepository.findAll().isEmpty()) {
			logger.info("Generating Semester data...");
			for (int i = 1; i <= 3; i++) {
				Semester semester = new Semester();
				semester.setStartDate(LocalDate.of(2025, i == 1 ? 1 : i == 2 ? 5 : 9, 1));
				semester.setEndDate(LocalDate.of(2025, i == 1 ? 4 : i == 2 ? 8 : 12, 30));
				semesters.add(semester);
			}
			semesterRepository.saveAll(semesters);
			logger.info("Semester data generated successfully.");
		} else {
			logger.warn("Database already contains Semester data. Using existing data.");
			semesters = semesterRepository.findAll();
		}

		// 7. Sinh dữ liệu cho ClassGroup (20 mẫu)
		List<ClassGroup> classGroups = new ArrayList<>();
		if (classGroupRepository.findAll().isEmpty()) {
			logger.info("Generating ClassGroup data...");
			for (int i = 1; i <= 20; i++) {
				ClassGroup classGroup = new ClassGroup();
				classGroup.setGroupCode(String.format("GROUP%03d", i));
				classGroup.setGroupName("Lớp " + String.format("%02d", i));
				classGroup.setMajor(majors.get((i - 1) % majors.size()));
				classGroup.setShift(shifts.get(random.nextInt(shifts.size())));
				classGroup.setSemester(semesters.get((i - 1) % semesters.size()));
				classGroups.add(classGroup);
			}
			classGroupRepository.saveAll(classGroups);
			logger.info("ClassGroup data generated successfully.");
		} else {
			logger.warn("Database already contains ClassGroup data. Using existing data.");
			classGroups = classGroupRepository.findAll();
		}

		// 8. Sinh dữ liệu cho Teacher (80 mẫu)
		List<Teacher> teachers = new ArrayList<>();
		if (teacherRepository.findAll().isEmpty()) {
			logger.info("Generating Teacher data...");
			for (int i = 1; i <= 80; i++) {
				Teacher teacher = new Teacher();
				teacher.setTeacherCode(String.format("GV%03d", i));
				String firstName = firstNames.get(random.nextInt(firstNames.size()));
				String lastName = lastNames.get(random.nextInt(lastNames.size()));
				teacher.setTeacherName(firstName + " " + lastName);
				teacher.setTeacherDateOfBirth(Date.valueOf(
						LocalDate.of(1965 + random.nextInt(35), 1 + random.nextInt(12), 1 + random.nextInt(28))));
				teacher.setTeacherGender(random.nextBoolean() ? Gender.Male : Gender.Female);
				teacher.setTeacherAddress(addresses.get(random.nextInt(addresses.size())));
				teacher.setTeacherPhoneNumber("09" + String.format("%08d", random.nextInt(100000000)));
				teacher.setTeacherEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.edu.vn");
				teacher.setDepartment(departments.get((i - 1) % departments.size()));
				teachers.add(teacher);
			}
			teacherRepository.saveAll(teachers);
			logger.info("Teacher data generated successfully.");
		} else {
			logger.warn("Database already contains Teacher data. Using existing data.");
			teachers = teacherRepository.findAll();
		}

		// 9. Sinh dữ liệu cho Subject (80 mẫu)
		List<Subject> subjects = new ArrayList<>();
		if (subjectRepository.findAll().isEmpty()) {
			logger.info("Generating Subject data...");
			String[] subjectNames = { "Toán cao cấp", "Vật lý đại cương", "Lập trình C++", "Cơ sở dữ liệu",
					"Kinh tế vi mô", "Kỹ thuật ô tô", "Giải phẫu học", "Sư phạm Toán", "Hóa học hữu cơ" };
			for (int i = 1; i <= 80; i++) {
				Subject subject = new Subject();
				subject.setSubjectCode(String.format("SUB%03d", i));
				subject.setSubjectName(subjectNames[(i - 1) % subjectNames.length] + " "
						+ String.format("%02d", (i - 1) / subjectNames.length + 1));
				subject.setCredits(2 + random.nextInt(3));
				subject.setSemester(semesters.get((i - 1) % semesters.size()));
				subject.setDeptId(departments.get((i - 1) % departments.size()).getId());
				subject.setDescription("Môn học " + subject.getSubjectName());
				subjects.add(subject);
			}
			subjectRepository.saveAll(subjects);
			logger.info("Subject data generated successfully.");
		} else {
			logger.warn("Database already contains Subject data. Using existing data.");
			subjects = subjectRepository.findAll();
		}

		// 10. Sinh dữ liệu cho Class (80 mẫu)
		List<Class> classes = new ArrayList<>();
		if (classRepository.findAll().isEmpty()) {
			logger.info("Generating Class data...");
			for (int i = 1; i <= 80; i++) {
				Class classEntity = new Class();
				classEntity.setClassCode(String.format("CLASS%03d", i));
				classEntity.setClassName("Lớp " + subjects.get(i - 1).getSubjectName());
				classEntity.setSubject(subjects.get(i - 1));
				classEntity.setTeacher(teachers.get((i - 1) % teachers.size()));
				classEntity.setClassGroup(classGroups.get((i - 1) % classGroups.size()));
				classEntity.setStartDate(semesters.get((i - 1) % semesters.size()).getStartDate());
				classEntity.setEndDate(semesters.get((i - 1) % semesters.size()).getEndDate());
				classEntity.setClassroom(classrooms.get((i - 1) % classrooms.size()));
				classEntity.setShift(shifts.get(random.nextInt(shifts.size())));
				classEntity.setPriority(1 + random.nextInt(10));
				classes.add(classEntity);
			}
			classRepository.saveAll(classes);
			logger.info("Class data generated successfully.");
		} else {
			logger.warn("Database already contains Class data. Using existing data.");
			classes = classRepository.findAll();
		}

		// 11. Sinh dữ liệu cho TimeWindow (80 mẫu)
		List<TimeWindow> timeWindows = new ArrayList<>();
		if (timeWindowRepository.findAll().isEmpty()) {
			logger.info("Generating TimeWindow data...");
			for (int i = 1; i <= 80; i++) {
				TimeWindow timeWindow = new TimeWindow();
				timeWindow.setIdClass(i);
				timeWindow.setDayOfWeek(daysOfWeek.get(random.nextInt(daysOfWeek.size())));
				timeWindow.setSlot(1 + random.nextInt(5));
				timeWindow.setCreatedDate(LocalDate.of(2025, 1, 1));
				timeWindow.setClazz(classes.get(i - 1));
				timeWindows.add(timeWindow);
			}
			timeWindowRepository.saveAll(timeWindows);
			logger.info("TimeWindow data generated successfully.");
		} else {
			logger.warn("Database already contains TimeWindow data. Using existing data.");
		}

		logger.info("Sample data generation completed successfully.");
	}
}