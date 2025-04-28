package com.studentApp.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@PersistenceContext
	private EntityManager entityManager;

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

	// Sửa danh sách shifts để khớp với ràng buộc trong cơ sở dữ liệu (morning,
	// afternoon)
	private final List<String> shifts = Arrays.asList("morning", "afternoon");

	private final List<String> daysOfWeek = Arrays.asList("Mon", "Tue", "Wed", "Thu", "Fri", "Sat");

	private final List<String> genders = Arrays.asList("Male", "Female");

	private final List<String> registrationStatuses = Arrays.asList("Registered", "Cancelled");

	private final List<String> attendanceStatuses = Arrays.asList("Present", "Absent", "Late");

	private final List<String> appealStatuses = Arrays.asList("Pending", "Approved", "Rejected");

	// Danh sách các khóa học (D17, D18, D19)
	private final List<String> courses = Arrays.asList("D17", "D18", "D19");

	// Ánh xạ giữa major_id và mã chuyên ngành
	private final Map<Long, String> majorCodeMap = new HashMap<>();

	// Ánh xạ giữa subject_id và mã môn học
	private final Map<Long, String> subjectCodeMap = new HashMap<>();

	// Constructor để khởi tạo ánh xạ mã chuyên ngành và mã môn học
	@Autowired
	public DataGeneratorService(EntityManager entityManager, ClassGroupRepository classGroupRepository,
			MajorRepository majorRepository, SemesterRepository semesterRepository,
			CurriculumRepository curriculumRepository, DepartmentRepository departmentRepository,
			TeacherRepository teacherRepository, SubjectRepository subjectRepository, ClassRepository classRepository,
			TimeWindowRepository timeWindowRepository, UserRepository userRepository, RoleRepository roleRepository,
			PermissionRepository permissionRepository) {
		this.entityManager = entityManager;
		this.classGroupRepository = classGroupRepository;
		this.majorRepository = majorRepository;
		this.semesterRepository = semesterRepository;
		this.curriculumRepository = curriculumRepository;
		this.departmentRepository = departmentRepository;
		this.teacherRepository = teacherRepository;
		this.subjectRepository = subjectRepository;
		this.classRepository = classRepository;
		this.timeWindowRepository = timeWindowRepository;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.permissionRepository = permissionRepository;

		// Khởi tạo ánh xạ mã chuyên ngành
		majorCodeMap.put(1L, "CNPM"); // Kỹ thuật phần mềm (Công nghệ Phần mềm)
		majorCodeMap.put(2L, "CNPM"); // Kỹ thuật phần mềm
		majorCodeMap.put(3L, "CNPM"); // Kỹ thuật phần mềm
		majorCodeMap.put(4L, "TMDT"); // Kinh tế quốc tế (Thương mại Điện tử)
		majorCodeMap.put(5L, "TMDT"); // Kinh tế quốc tế
		majorCodeMap.put(6L, "KTO"); // Kỹ thuật ô tô
		majorCodeMap.put(7L, "KTO"); // Kỹ thuật ô tô
		majorCodeMap.put(8L, "YDK"); // Y đa khoa
		majorCodeMap.put(9L, "YDK"); // Y đa khoa
		majorCodeMap.put(10L, "SPT"); // Sư phạm Toán
		majorCodeMap.put(11L, "SPT"); // Sư phạm Toán
		majorCodeMap.put(12L, "HOA"); // Hóa học

		// Khởi tạo ánh xạ mã môn học (dựa trên subjectNames trong phần 9)
		subjectCodeMap.put(1L, "TC"); // Toán cao cấp
		subjectCodeMap.put(2L, "VL"); // Vật lý đại cương
		subjectCodeMap.put(3L, "LTC"); // Lập trình C++
		subjectCodeMap.put(4L, "CSDL"); // Cơ sở dữ liệu
		subjectCodeMap.put(5L, "KTVM"); // Kinh tế vi mô
		subjectCodeMap.put(6L, "KTO"); // Kỹ thuật ô tô
		subjectCodeMap.put(7L, "GPH"); // Giải phẫu học
		subjectCodeMap.put(8L, "SPT"); // Sư phạm Toán
		subjectCodeMap.put(9L, "HHC"); // Hóa học hữu cơ
	}

	// Lấy ID lớn nhất từ bảng
	private long getNextId(String tableName) {
		try {
			Object result = entityManager.createNativeQuery("SELECT COALESCE(MAX(id), 0) + 1 FROM " + tableName)
					.getSingleResult();
			return ((Number) result).longValue();
		} catch (Exception e) {
			logger.error("Error getting next ID for table {}: {}", tableName, e.getMessage());
			return 1; // Nếu bảng rỗng hoặc lỗi, bắt đầu từ 1
		}
	}

	@Transactional
	public void generateSampleData() {
		logger.info("Starting to generate sample data...");

		// 0. Sinh dữ liệu cho Permission (các quyền)
		logger.info("Generating Permission data...");
		List<Permission> permissions = new ArrayList<>();
		String[] permissionNames = { "DATA_GENERATE", "CURRICULUM_TEMPLATE", "CLASS_CREATE", "SUBJECT_VIEW",
				"TEACHER_MANAGE", "DEPARTMENT_EDIT", "MAJOR_CREATE", "SEMESTER_VIEW" };
		for (String permissionName : permissionNames) {
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_permission WHERE permission_name = :permission_name")
					.setParameter("permission_name", permissionName).getSingleResult();
			if (count == 0) {
				Permission permission = new Permission();
				permission.setPermissionName(permissionName);
				permission.setDescription("Permission for " + permissionName.toLowerCase().replace("_", " "));
				permissions.add(permission);
			} else {
				logger.warn("Permission {} already exists, skipping...", permissionName);
			}
		}
		if (!permissions.isEmpty()) {
			permissionRepository.saveAll(permissions);
			logger.info("Permission data generated successfully.");
		} else {
			logger.info("No new permissions to generate.");
		}
		List<Permission> existingPermissions = permissionRepository.findAll();

		// 1. Sinh dữ liệu cho Role (vai trò)
		logger.info("Generating Role data...");
		List<Role> roles = new ArrayList<>();
		String[] roleNames = { "ROLE_ADMIN", "ROLE_USER" };
		for (int i = 0; i < roleNames.length; i++) {
			String roleName = roleNames[i];
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_role WHERE role_name = :role_name")
					.setParameter("role_name", roleName).getSingleResult();
			if (count == 0) {
				Role role = new Role();
				role.setRoleName(roleName);
				role.setDescription(
						i == 0 ? "Administrator role with full access" : "Regular user role with limited access");
				role.setPermissions(i == 0 ? new ArrayList<>(existingPermissions)
						: new ArrayList<>(existingPermissions.subList(0, 2)));
				role.setCreatedAt(LocalDateTime.now());
				role.setUpdatedAt(LocalDateTime.now());
				roles.add(role);
			} else {
				logger.warn("Role {} already exists, skipping...", roleName);
			}
		}
		if (!roles.isEmpty()) {
			try {
				roleRepository.saveAll(roles);
				logger.info("Role data generated successfully.");
			} catch (Exception e) {
				logger.error("Failed to generate Role data: {}", e.getMessage());
				throw e; // Re-throw to ensure the transaction rolls back properly
			}
		} else {
			logger.info("No new roles to generate.");
		}
		List<Role> existingRoles = roleRepository.findAll();

		// 2. Sinh dữ liệu cho User (tài khoản)
		logger.info("Generating User data...");
		List<User> users = new ArrayList<>();
		String[][] userData = {
				{ "admin1", "admin123", "admin1@university.edu.vn", "https://example.com/avatars/admin1.jpg", "0" },
				{ "user1", "user123", "user1@university.edu.vn", "https://example.com/avatars/user1.jpg", "1" } };
		for (String[] data : userData) {
			String username = data[0];
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_user WHERE username = :username")
					.setParameter("username", username).getSingleResult();
			if (count == 0) {
				User user = new User();
				user.setUsername(username);
				user.setPassword(passwordEncoder.encode(data[1]));
				user.setEmail(data[2]);
				user.setAvatarUrl(data[3]);
				user.setCreatedAt(LocalDateTime.now());
				user.setUpdatedAt(LocalDateTime.now());
				user.setRole(existingRoles.get(Integer.parseInt(data[4])));
				users.add(user);
			} else {
				logger.warn("User {} already exists, skipping...", username);
			}
		}
		if (!users.isEmpty()) {
			userRepository.saveAll(users);
			logger.info("User data generated successfully.");
		} else {
			logger.info("No new users to generate.");
		}

		// 3. Sinh dữ liệu cho Curriculum (5 mẫu)
		logger.info("Generating Curriculum data...");
		List<Curriculum> curriculums = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			String curriculumCode = String.format("CURR%03d", i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_curriculum WHERE curriculum_code = :curriculum_code")
					.setParameter("curriculum_code", curriculumCode).getSingleResult();
			if (count == 0) {
				Curriculum curriculum = new Curriculum();
				curriculum.setCurriculumCode(curriculumCode);
				curriculum.setCurriculumName("Chương trình đào tạo " + (i == 1 ? "Công nghệ thông tin"
						: i == 2 ? "Kinh tế" : i == 3 ? "Kỹ thuật" : i == 4 ? "Y học" : "Giáo dục"));
				curriculum.setDescription("Chương trình đào tạo " + curriculum.getCurriculumName());
				curriculum.setCreatedAt(LocalDateTime.now());
				curriculum.setUpdatedAt(LocalDateTime.now());
				curriculums.add(curriculum);
			} else {
				logger.warn("Curriculum {} already exists, skipping...", curriculumCode);
			}
		}
		if (!curriculums.isEmpty()) {
			curriculumRepository.saveAll(curriculums);
			logger.info("Curriculum data generated successfully.");
		} else {
			logger.info("No new curriculums to generate.");
		}
		List<Curriculum> existingCurriculums = curriculumRepository.findAll();

		// 4. Sinh dữ liệu cho Department (6 mẫu)
		logger.info("Generating Department data...");
		List<Department> departments = new ArrayList<>();
		for (int i = 1; i <= 6; i++) {
			String deptCode = String.format("KHOA%03d", i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_department WHERE dept_code = :dept_code")
					.setParameter("dept_code", deptCode).getSingleResult();
			if (count == 0) {
				Department department = new Department();
				department.setDeptCode(deptCode);
				department.setDeptName(i == 1 ? "Khoa Công nghệ thông tin"
						: i == 2 ? "Khoa Kinh tế"
								: i == 3 ? "Khoa Cơ khí"
										: i == 4 ? "Khoa Y" : i == 5 ? "Khoa Sư phạm" : "Khoa Khoa học tự nhiên");
				department.setDescription("Khoa " + department.getDeptName());
				department.setCreatedAt(LocalDateTime.now());
				department.setUpdatedAt(LocalDateTime.now());
				departments.add(department);
			} else {
				logger.warn("Department {} already exists, skipping...", deptCode);
			}
		}
		if (!departments.isEmpty()) {
			try {
				departmentRepository.saveAll(departments);
				logger.info("Department data generated successfully.");
			} catch (Exception e) {
				logger.error("Failed to generate Department data: {}", e.getMessage());
				throw e; // Re-throw to ensure the transaction rolls back properly
			}
		} else {
			logger.info("No new departments to generate.");
		}
		List<Department> existingDepartments = departmentRepository.findAll();

		// 5. Sinh dữ liệu cho Major (12 mẫu)
		logger.info("Generating Major data...");
		List<Major> majors = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			String majorCode = String.format("MAJ%03d", i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_major WHERE major_code = :major_code")
					.setParameter("major_code", majorCode).getSingleResult();
			if (count == 0) {
				Major major = new Major();
				major.setMajorCode(majorCode);
				major.setMajorName(i <= 3 ? "Kỹ thuật phần mềm"
						: i <= 5 ? "Kinh tế quốc tế"
								: i <= 7 ? "Kỹ thuật ô tô"
										: i <= 9 ? "Y đa khoa" : i <= 11 ? "Sư phạm Toán" : "Hóa học");
				major.setDeptId(existingDepartments.get((i - 1) % existingDepartments.size()).getId());
				major.setCurriculum(existingCurriculums.get((i - 1) / 3));
				major.setDescription("Chuyên ngành " + major.getMajorName());
				major.setCreatedAt(LocalDateTime.now());
				major.setUpdatedAt(LocalDateTime.now());
				majors.add(major);
			} else {
				logger.warn("Major {} already exists, skipping...", majorCode);
			}
		}
		if (!majors.isEmpty()) {
			try {
				majorRepository.saveAll(majors);
				logger.info("Major data generated successfully.");
			} catch (Exception e) {
				logger.error("Failed to generate Major data: {}", e.getMessage());
				throw e; // Re-throw to ensure the transaction rolls back properly
			}
		} else {
			logger.info("No new majors to generate.");
		}
		List<Major> existingMajors = majorRepository.findAll();

		// 6. Sinh dữ liệu cho Semester (3 mẫu)
		logger.info("Generating Semester data...");
		List<Semester> semesters = new ArrayList<>();
		for (int i = 1; i <= 3; i++) {
			LocalDate startDate = LocalDate.of(2025, i == 1 ? 1 : i == 2 ? 5 : 9, 1);
			LocalDate endDate = LocalDate.of(2025, i == 1 ? 4 : i == 2 ? 8 : 12, 30);
			Long count = (Long) entityManager
					.createNativeQuery(
							"SELECT COUNT(*) FROM tbl_semester WHERE start_date = :start_date AND end_date = :end_date")
					.setParameter("start_date", startDate).setParameter("end_date", endDate).getSingleResult();
			if (count == 0) {
				Semester semester = new Semester();
				semester.setStartDate(startDate);
				semester.setEndDate(endDate);
				semesters.add(semester);
			} else {
				logger.warn("Semester from {} to {} already exists, skipping...", startDate, endDate);
			}
		}
		if (!semesters.isEmpty()) {
			semesterRepository.saveAll(semesters);
			logger.info("Semester data generated successfully.");
		} else {
			logger.info("No new semesters to generate.");
		}
		List<Semester> existingSemesters = semesterRepository.findAll();

		// 7. Sinh dữ liệu cho ClassGroup
		logger.info("Generating ClassGroup data...");
		List<ClassGroup> classGroups = new ArrayList<>();

		// Mỗi chuyên ngành sẽ có 2-4 lớp cho mỗi khóa (D17, D18, D19)
		if (existingMajors.isEmpty() || existingSemesters.isEmpty()) {
			logger.warn("No majors or semesters found. Please generate those first.");
			return;
		}

		for (Major major : existingMajors) {
			String majorCode = majorCodeMap.get(major.getId());
			if (majorCode == null) {
				logger.warn("No major code mapping found for major_id: {}", major.getId());
				continue;
			}

			for (String course : courses) {
				// Số lượng lớp ngẫu nhiên (từ 2 đến 4 lớp cho mỗi chuyên ngành, mỗi khóa)
				int numClasses = random.nextInt(3) + 2; // 2, 3, hoặc 4 lớp
				for (int i = 1; i <= numClasses; i++) {
					// Tạo group_code: D<khoa><chuyên ngành><số thứ tự>
					String groupCode = String.format("%s%s%d", course, majorCode, i);
					Long count = (Long) entityManager
							.createNativeQuery("SELECT COUNT(*) FROM tbl_class_group WHERE group_code = :group_code")
							.setParameter("group_code", groupCode).getSingleResult();

					if (count == 0) {
						ClassGroup classGroup = new ClassGroup();
						classGroup.setGroupCode(groupCode);
						classGroup.setGroupName("Lớp " + groupCode); // Tên lớp: "Lớp D17CNPM1", "Lớp D18TMDT2", v.v.
						classGroup.setMajor(major);
						classGroup.setShift(shifts.get(random.nextInt(shifts.size()))); // "morning" hoặc "afternoon"
						classGroup.setSemester(existingSemesters.get(random.nextInt(existingSemesters.size())));
						classGroups.add(classGroup);
						logger.info("Created ClassGroup: group_code={}, group_name={}", groupCode,
								classGroup.getGroupName());
					} else {
						logger.warn("ClassGroup {} already exists, skipping...", groupCode);
					}
				}
			}
		}

		// Lưu dữ liệu
		if (!classGroups.isEmpty()) {
			try {
				classGroupRepository.saveAll(classGroups);
				logger.info("ClassGroup data generated successfully.");
			} catch (Exception e) {
				logger.error("Failed to generate ClassGroup data: {}", e.getMessage());
				throw e;
			}
		} else {
			logger.info("No new class groups to generate.");
		}
		List<ClassGroup> existingClassGroups = classGroupRepository.findAll();

		// 8. Sinh dữ liệu cho Teacher (80 mẫu)
		logger.info("Generating Teacher data...");
		List<Teacher> teachers = new ArrayList<>();
		for (int i = 1; i <= 80; i++) {
			String teacherCode = String.format("GV%03d", i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_teacher WHERE teacher_code = :teacher_code")
					.setParameter("teacher_code", teacherCode).getSingleResult();
			if (count == 0) {
				Teacher teacher = new Teacher();
				teacher.setTeacherCode(teacherCode);
				String firstName = firstNames.get(random.nextInt(firstNames.size()));
				String lastName = lastNames.get(random.nextInt(lastNames.size()));
				teacher.setTeacherName(firstName + " " + lastName);
				teacher.setTeacherDateOfBirth(Date.valueOf(
						LocalDate.of(1965 + random.nextInt(35), 1 + random.nextInt(12), 1 + random.nextInt(28))));
				teacher.setTeacherGender(random.nextBoolean() ? Gender.Male : Gender.Female);
				teacher.setTeacherAddress(addresses.get(random.nextInt(addresses.size())));
				teacher.setTeacherPhoneNumber("09" + String.format("%08d", random.nextInt(100000000)));
				teacher.setTeacherEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@university.edu.vn");
				teacher.setDepartment(existingDepartments.get((i - 1) % existingDepartments.size()));
				teachers.add(teacher);
			} else {
				logger.warn("Teacher {} already exists, skipping...", teacherCode);
			}
		}
		if (!teachers.isEmpty()) {
			teacherRepository.saveAll(teachers);
			logger.info("Teacher data generated successfully.");
		} else {
			logger.info("No new teachers to generate.");
		}
		List<Teacher> existingTeachers = teacherRepository.findAll();

		// 9. Sinh dữ liệu cho Subject (80 mẫu)
		logger.info("Generating Subject data...");
		List<Subject> subjects = new ArrayList<>();
		String[] subjectNames = { "Toán cao cấp", "Vật lý đại cương", "Lập trình C++", "Cơ sở dữ liệu", "Kinh tế vi mô",
				"Kỹ thuật ô tô", "Giải phẫu học", "Sư phạm Toán", "Hóa học hữu cơ" };
		for (int i = 1; i <= 80; i++) {
			String subjectCode = String.format("SUB%03d", i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_subject WHERE subject_code = :subject_code")
					.setParameter("subject_code", subjectCode).getSingleResult();
			if (count == 0) {
				Subject subject = new Subject();
				subject.setSubjectCode(subjectCode);
				subject.setSubjectName(subjectNames[(i - 1) % subjectNames.length] + " "
						+ String.format("%02d", (i - 1) / subjectNames.length + 1));
				subject.setCredits(2 + random.nextInt(3));
				subject.setSemester(existingSemesters.get((i - 1) % existingSemesters.size()));
				subject.setDeptId(existingDepartments.get((i - 1) % existingDepartments.size()).getId());
				subject.setDescription("Môn học " + subject.getSubjectName());
				subjects.add(subject);

				// Cập nhật subjectCodeMap cho các môn đầu tiên (1-9)
				if (i <= 9) {
					subjectCodeMap.put((long) i, subjectCodeMap.getOrDefault((long) ((i - 1) % 9 + 1), "UNKNOWN"));
				}
			} else {
				logger.warn("Subject {} already exists, skipping...", subjectCode);
			}
		}
		if (!subjects.isEmpty()) {
			subjectRepository.saveAll(subjects);
			logger.info("Subject data generated successfully.");
		} else {
			logger.info("No new subjects to generate.");
		}
		List<Subject> existingSubjects = subjectRepository.findAll();

		// 10. Sinh dữ liệu cho Class (80 mẫu)
		logger.info("Generating Class data...");
		List<Class> classes = new ArrayList<>();

		if (existingSubjects.isEmpty() || existingTeachers.isEmpty() || existingClassGroups.isEmpty()) {
			logger.warn("No subjects, teachers, or class groups found. Please generate those first.");
			return;
		}

		for (int i = 0; i < 80; i++) {
			ClassGroup classGroup = existingClassGroups.get(i % existingClassGroups.size());
			Subject subject = existingSubjects.get(i % existingSubjects.size());
			// Tạo mã môn học ngắn gọn từ subjectCodeMap (hoặc mặc định nếu không có)
			String subjectShortCode = subjectCodeMap.getOrDefault(subject.getId() % 9 == 0 ? 9 : subject.getId() % 9,
					"SUB");
			// Tạo class_code: <group_code>-<subjectShortCode><subject_id>
			String classCode = String.format("%s-%s%03d", classGroup.getGroupCode(), subjectShortCode, subject.getId());
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_class WHERE class_code = :class_code")
					.setParameter("class_code", classCode).getSingleResult();

			if (count == 0) {
				Class classEntity = new Class();
				classEntity.setClassCode(classCode);
				classEntity.setClassName("Lớp " + classGroup.getGroupCode() + " - " + subject.getSubjectName());
				classEntity.setSubject(subject);
				classEntity.setTeacher(existingTeachers.get(i % existingTeachers.size()));
				classEntity.setClassGroup(classGroup);
				classEntity.setStartDate(classGroup.getSemester().getStartDate());
				classEntity.setEndDate(classGroup.getSemester().getEndDate());
				classEntity.setClassroom(classrooms.get(i % classrooms.size()));
				classEntity.setShift(shifts.get(random.nextInt(shifts.size()))); // "morning" hoặc "afternoon"
				classEntity.setPriority(1 + random.nextInt(10));
				classes.add(classEntity);
				logger.info("Created Class: class_code={}, class_name={}", classCode, classEntity.getClassName());
			} else {
				logger.warn("Class {} already exists, skipping...", classCode);
			}
		}

		if (!classes.isEmpty()) {
			try {
				classRepository.saveAll(classes);
				logger.info("Class data generated successfully.");
			} catch (Exception e) {
				logger.error("Failed to generate Class data: {}", e.getMessage());
				throw e;
			}
		} else {
			logger.info("No new classes to generate.");
		}
		List<Class> existingClasses = classRepository.findAll();

		// 11. Sinh dữ liệu cho TimeWindow (80 mẫu)
		logger.info("Generating TimeWindow data...");
		List<TimeWindow> timeWindows = new ArrayList<>();
		for (int i = 0; i < 80; i++) {
			TimeWindow timeWindow = new TimeWindow();
			// Set composite key fields
			timeWindow.setIdClass(existingClasses.get(i % existingClasses.size()).getId()); // No conversion needed
			timeWindow.setDayOfWeek(daysOfWeek.get(random.nextInt(daysOfWeek.size())));
			timeWindow.setSlot(1 + random.nextInt(5));
			// Set other fields
			timeWindow.setCreatedDate(LocalDate.of(2025, 1, 1));
			timeWindow.setClazz(existingClasses.get(i % existingClasses.size()));
			timeWindows.add(timeWindow);
		}
		if (!timeWindows.isEmpty()) {
			timeWindowRepository.saveAll(timeWindows);
			logger.info("TimeWindow data generated successfully.");
		} else {
			logger.info("No new time windows to generate.");
		}

		// 12. Sinh dữ liệu cho tbl_student (100 mẫu)
		logger.info("Generating Student data...");
		long nextStudentId = getNextId("tbl_student");
		for (int i = 0; i < 100; i++) {
			String studentCode = String.format("SV%03d", nextStudentId + i);
			Long count = (Long) entityManager
					.createNativeQuery("SELECT COUNT(*) FROM tbl_student WHERE student_code = :student_code")
					.setParameter("student_code", studentCode).getSingleResult();
			if (count == 0) {
				String firstName = firstNames.get(random.nextInt(firstNames.size()));
				String lastName = lastNames.get(random.nextInt(lastNames.size()));
				entityManager.createNativeQuery(
						"INSERT INTO tbl_student (id, student_code, student_name, date_of_birth, gender, address, phone_number, email, class_group_id) "
								+ "VALUES (:id, :student_code, :student_name, :date_of_birth, :gender, :address, :phone_number, :email, :class_group_id)")
						.setParameter("id", nextStudentId + i).setParameter("student_code", studentCode)
						.setParameter("student_name", firstName + " " + lastName)
						.setParameter("date_of_birth",
								Date.valueOf(LocalDate.of(2000 + random.nextInt(5), 1 + random.nextInt(12),
										1 + random.nextInt(28))))
						.setParameter("gender", genders.get(random.nextInt(genders.size())))
						.setParameter("address", addresses.get(random.nextInt(addresses.size())))
						.setParameter("phone_number", "09" + String.format("%08d", random.nextInt(100000000)))
						.setParameter("email",
								firstName.toLowerCase() + "." + lastName.toLowerCase() + "@student.university.edu.vn")
						.setParameter("class_group_id", existingClassGroups.get(i % existingClassGroups.size()).getId())
						.executeUpdate();
			} else {
				logger.warn("Student {} already exists, skipping...", studentCode);
			}
		}
		logger.info("Student data generated successfully.");

		// 13. Sinh dữ liệu cho tbl_registration (200 mẫu)
		logger.info("Generating Registration data...");
		long nextRegistrationId = getNextId("tbl_registration");
		for (int i = 0; i < 200; i++) {
			long studentId = (nextStudentId + (i % 100));
			entityManager.createNativeQuery(
					"INSERT INTO tbl_registration (id, class_id, student_id, semester_id, registration_date, status) "
							+ "VALUES (:id, :class_id, :student_id, :semester_id, :registration_date, :status)")
					.setParameter("id", nextRegistrationId + i)
					.setParameter("class_id", existingClasses.get(i % existingClasses.size()).getId())
					.setParameter("student_id", studentId)
					.setParameter("semester_id", existingSemesters.get(i % existingSemesters.size()).getId())
					.setParameter("registration_date", LocalDate.of(2025, 1, 1).plusDays(random.nextInt(30)))
					.setParameter("status", registrationStatuses.get(random.nextInt(registrationStatuses.size())))
					.executeUpdate();
		}
		logger.info("Registration data generated successfully.");

		// 14. Sinh dữ liệu cho tbl_teacher_subject_registration (80 mẫu)
		logger.info("Generating TeacherSubjectRegistration data...");
		long nextTeacherSubjectRegistrationId = getNextId("tbl_teacher_subject_registration");
		for (int i = 0; i < 80; i++) {
			entityManager
					.createNativeQuery(
							"INSERT INTO tbl_teacher_subject_registration (id, teacher_id, subject_id, semester_id) "
									+ "VALUES (:id, :teacher_id, :subject_id, :semester_id)")
					.setParameter("id", nextTeacherSubjectRegistrationId + i)
					.setParameter("teacher_id", existingTeachers.get(i % existingTeachers.size()).getId())
					.setParameter("subject_id", existingSubjects.get(i % existingSubjects.size()).getId())
					.setParameter("semester_id", existingSemesters.get(i % existingSemesters.size()).getId())
					.executeUpdate();
		}
		logger.info("TeacherSubjectRegistration data generated successfully.");

		// 15. Sinh dữ liệu cho tbl_attendance (200 mẫu)
		logger.info("Generating Attendance data...");
		long nextAttendanceId = getNextId("tbl_attendance");
		for (int i = 0; i < 200; i++) {
			long studentId = (nextStudentId + (i % 100));
			entityManager
					.createNativeQuery(
							"INSERT INTO tbl_attendance (id, class_id, student_id, attendance_date, status, note) "
									+ "VALUES (:id, :class_id, :student_id, :attendance_date, :status, :note)")
					.setParameter("id", nextAttendanceId + i)
					.setParameter("class_id", existingClasses.get(i % existingClasses.size()).getId())
					.setParameter("student_id", studentId)
					.setParameter("attendance_date", LocalDate.of(2025, 1, 1).plusDays(random.nextInt(90)))
					.setParameter("status", attendanceStatuses.get(random.nextInt(attendanceStatuses.size())))
					.setParameter("note", "Ghi chú: " + (random.nextBoolean() ? "Đi muộn" : "Đúng giờ"))
					.executeUpdate();
		}
		logger.info("Attendance data generated successfully.");

		// 16. Sinh dữ liệu cho tbl_grade (200 mẫu)
		logger.info("Generating Grade data...");
		long nextGradeId = getNextId("tbl_grade");
		for (int i = 0; i < 200; i++) {
			long studentId = (nextStudentId + (i % 100));
			float attendanceScore = 5 + random.nextFloat() * 5; // 5.0 - 10.0
			float examScore = 5 + random.nextFloat() * 5;
			float finalScore = (attendanceScore * 0.3f + examScore * 0.7f);
			entityManager.createNativeQuery(
					"INSERT INTO tbl_grade (id, class_id, student_id, attendance_score, exam_score, final_score, note) "
							+ "VALUES (:id, :class_id, :student_id, :attendance_score, :exam_score, :final_score, :note)")
					.setParameter("id", nextGradeId + i)
					.setParameter("class_id", existingClasses.get(i % existingClasses.size()).getId())
					.setParameter("student_id", studentId).setParameter("attendance_score", attendanceScore)
					.setParameter("exam_score", examScore).setParameter("final_score", finalScore)
					.setParameter("note", "Ghi chú: " + (finalScore >= 5 ? "Đạt" : "Cần cải thiện")).executeUpdate();
		}
		logger.info("Grade data generated successfully.");

		// 17. Sinh dữ liệu cho tbl_grade_appeal (50 mẫu)
		logger.info("Generating GradeAppeal data...");
		long nextGradeAppealId = getNextId("tbl_grade_appeal");
		for (int i = 0; i < 50; i++) {
			long studentId = (nextStudentId + (i % 100));
			entityManager.createNativeQuery(
					"INSERT INTO tbl_grade_appeal (id, student_id, class_id, reason, status, submitted_at, processed_at) "
							+ "VALUES (:id, :student_id, :class_id, :reason, :status, :submitted_at, :processed_at)")
					.setParameter("id", nextGradeAppealId + i).setParameter("student_id", studentId)
					.setParameter("class_id", existingClasses.get(i % existingClasses.size()).getId())
					.setParameter("reason", "Điểm thi không chính xác #" + (nextGradeAppealId + i))
					.setParameter("status", appealStatuses.get(random.nextInt(appealStatuses.size())))
					.setParameter("submitted_at", LocalDateTime.of(2025, 1, 1, 10, 0).plusDays(random.nextInt(90)))
					.setParameter("processed_at", LocalDateTime.of(2025, 1, 1, 10, 0).plusDays(random.nextInt(90) + 5))
					.executeUpdate();
		}
		logger.info("GradeAppeal data generated successfully.");

		// 18. Sinh dữ liệu cho tbl_grade_history (50 mẫu)
		logger.info("Generating GradeHistory data...");
		long nextGradeHistoryId = getNextId("tbl_grade_history");
		for (int i = 0; i < 50; i++) {
			float oldScore = 5 + random.nextFloat() * 5;
			float newScore = oldScore + random.nextFloat() * 2 - 1; // Thay đổi ±1 điểm
			entityManager.createNativeQuery(
					"INSERT INTO tbl_grade_history (id, teacher_id, grade_id, old_score, new_score, updated_at, reason) "
							+ "VALUES (:id, :teacher_id, :grade_id, :old_score, :new_score, :updated_at, :reason)")
					.setParameter("id", nextGradeHistoryId + i)
					.setParameter("teacher_id", existingTeachers.get(i % existingTeachers.size()).getId())
					.setParameter("grade_id", nextGradeId + (i % 200)).setParameter("old_score", oldScore)
					.setParameter("new_score", newScore)
					.setParameter("updated_at", LocalDateTime.of(2025, 1, 1, 10, 0).plusDays(random.nextInt(90)))
					.setParameter("reason", "Sửa lỗi chấm điểm #" + (nextGradeHistoryId + i)).executeUpdate();
		}
		logger.info("GradeHistory data generated successfully.");

		logger.info("Sample data generation completed successfully.");
	}
}