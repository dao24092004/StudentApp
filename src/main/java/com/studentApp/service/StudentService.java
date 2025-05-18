package com.studentApp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.StudentCreationRequest;
import com.studentApp.dto.request.StudentUpdateRequest;
import com.studentApp.dto.response.StudentResponse;
import com.studentApp.entity.ClassGroup;
import com.studentApp.entity.Major;
import com.studentApp.entity.Role;
import com.studentApp.entity.Student;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.enums.Gender;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.StudentMapper;
import com.studentApp.repository.ClassGroupRepository;
import com.studentApp.repository.MajorRepository;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.StudentRepository;
import com.studentApp.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;

@Service
public class StudentService {

	private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

	private final JwtService jwtService;

	@Autowired
	private StudentRepository studentRepository;

	@Autowired
	private MajorRepository majorRepository;

	@Autowired
	private ClassGroupRepository classGroupRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@PersistenceContext
	private EntityManager entityManager;

	public StudentService(JwtService jwtService) {
		this.jwtService = jwtService;
	}

	public List<StudentResponse> getAllStudent() {
		List<Student> students = studentRepository.findAll();
		List<StudentResponse> response = new ArrayList<>();
		for (Student student : students) {
			response.add(StudentMapper.toStudentResponse(student));
		}
		return response;
	}

	public StudentResponse getIDStudent(Long id) {
		if (id == null) {
			throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Student ID must not be null");
		}
		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
		return StudentMapper.toStudentResponse(student);
	}

	@Transactional
	public StudentResponse createStudent(@Valid StudentCreationRequest request) {
		// Kiểm tra trùng lặp phoneNumber
		if (request.getPhone_number() != null && studentRepository.existsByPhoneNumber(request.getPhone_number())) {
			logger.warn("Student with phone number {} already exists", request.getPhone_number());
			throw new AppException(ErrorCode.STUDENT_ALREADY_EXISTS,
					"Student with phone number '" + request.getPhone_number() + "' already exists.");
		}

		// Kiểm tra trùng lặp email
		if (userRepository.existsByEmail(request.getEmailUser())) {
			logger.warn("User with email {} already exists", request.getEmailUser());
			throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS,
					"User with email '" + request.getEmailUser() + "' already exists.");
		}

		// Kiểm tra majorId
		Major major = majorRepository.findById(request.getMajor_id()).orElseThrow(() -> {
			logger.warn("Major with ID {} not found", request.getMajor_id());
			return new AppException(ErrorCode.MAJOR_NOT_FOUND,
					"Major with ID " + request.getMajor_id() + " not found.");
		});

		// Kiểm tra classGroupId
		ClassGroup classGroup = classGroupRepository.findById(request.getClass_group_id()).orElseThrow(() -> {
			logger.warn("Class group with ID {} not found", request.getClass_group_id());
			return new AppException(ErrorCode.CLASS_NOT_FOUND,
					"Class group with ID " + request.getClass_group_id() + " not found.");
		});

		// Tạo user mới
		User user = new User();
		user.setUsername(request.getStudent_name());
		user.setPassword(jwtService.encodePassword("password123"));
		user.setEmail(request.getEmailUser());

		Role studentRoleEntity = roleRepository.findByRoleName("STUDENT")
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
		user.setRole(studentRoleEntity);

		// Tạo student
		Student student = new Student();
		student.setStudentName(request.getStudent_name());
		student.setDateOfBirth(request.getDate_of_birth());

		// Kiểm tra gender hợp lệ
		List<String> validGenders = Arrays.asList("Male", "Female");
		if (!validGenders.contains(request.getGender())) {
			throw new AppException(ErrorCode.INVALID_REQUEST, "Gender must be one of: Male, Female");
		}
		Gender gender = Gender.valueOf(request.getGender());
		student.setGender(gender);

		student.setAddress(request.getAddress());
		student.setPhoneNumber(request.getPhone_number());
		student.setMajor(major);
		student.setClassGroup(classGroup);
		student.setUser(user);

		// Lưu user trước để có user_id
		User savedUser = userRepository.save(user);
		student.setUser(savedUser);

		// Sinh studentCode tạm thời
		String tempStudentCode = "TEMP_" + System.currentTimeMillis();
		student.setStudentCode(tempStudentCode);

		// Lưu student lần đầu
		Student savedStudent = studentRepository.save(student);

		// Sinh studentCode chính thức dựa trên id
		String studentCode = generateStudentCode(savedStudent.getId());
		savedStudent.setStudentCode(studentCode);

		// Lưu lại student với studentCode đã sinh
		savedStudent = studentRepository.save(savedStudent);
		logger.info("Created student with ID {} and studentCode {}", savedStudent.getId(),
				savedStudent.getStudentCode());

		return StudentMapper.toStudentResponse(savedStudent);
	}

	@Transactional
	public StudentResponse updateStudent(Long id, @Valid StudentUpdateRequest request) {
		if (id == null) {
			throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Student ID must not be null");
		}

		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		if (request.getPhoneNumber() != null) {
			studentRepository.findByPhoneNumber(request.getPhoneNumber()).ifPresent(existing -> {
				if (!existing.getId().equals(id)) {
					logger.warn("Student with phone number {} already exists", request.getPhoneNumber());
					throw new AppException(ErrorCode.STUDENT_ALREADY_EXISTS,
							"Student with phone number '" + request.getPhoneNumber() + "' already exists.");
				}
			});
			student.setPhoneNumber(request.getPhoneNumber());
		}

		if (request.getStudentName() != null) {
			student.setStudentName(request.getStudentName());
		}

		if (request.getAddress() != null) {
			student.setAddress(request.getAddress());
		}

		if (request.getDateOfBirth() != null) {
			student.setDateOfBirth(request.getDateOfBirth());
		}

		if (request.getGender() != null) {
			List<String> validGenders = Arrays.asList("Male", "Female");
			if (!validGenders.contains(request.getGender())) {
				throw new AppException(ErrorCode.INVALID_REQUEST, "Gender must be one of: Male, Female");
			}
			Gender gender = Gender.valueOf(request.getGender());
			student.setGender(gender);
		}

		Student updatedStudent = studentRepository.save(student);
		logger.info("Updated student with ID {}", updatedStudent.getId());
		return StudentMapper.toStudentResponse(updatedStudent);
	}

	@Transactional
	public void deleteStudent(Long id) {
		if (id == null) {
			throw new AppException(ErrorCode.INVALID_CREDENTIALS, "Student ID must not be null");
		}

		Student student = studentRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

		studentRepository.delete(student);
		logger.info("Deleted student with ID {}", id);
	}

	private String generateStudentCode(Long id) {
		int currentYear = java.time.Year.now().getValue();
		return "SV" + currentYear + String.format("%05d", id);
	}
}