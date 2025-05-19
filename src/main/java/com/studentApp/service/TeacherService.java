package com.studentApp.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.request.TeacherUpdateRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.entity.Department;
import com.studentApp.entity.Role;
import com.studentApp.entity.Teacher;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.mapper.TeacherMapper;
import com.studentApp.repository.DepartmentRepository;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.UserRepository;

@Service
public class TeacherService {

	private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

	@Autowired
	private TeacherRepository teacherRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private JwtService jwtService;

	public List<TeacherResponse> getAllTeacher() {
		List<Teacher> teachers = teacherRepository.findAll();
		List<TeacherResponse> responses = new ArrayList<>();
		for (Teacher teacher : teachers) {
			responses.add(TeacherMapper.toTeacherResponse(teacher));
		}
		return responses;
	}

	public TeacherResponse getByIdTeacher(long id) {
		Teacher teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
		return TeacherMapper.toTeacherResponse(teacher);
	}

	@Transactional
	public TeacherResponse createTeacher(TeacherCreationRequest request) {
		// Kiểm tra trùng lặp phone number
		if (teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber()).isPresent()) {
			logger.warn("Teacher with phone number {} already exists", request.getTeacherPhoneNumber());
			throw new AppException(ErrorCode.TEACHER_ALLREADY_EXISTS,
					"Teacher with phone number '" + request.getTeacherPhoneNumber() + "' already exists.");
		}

		// Kiểm tra deptId
		Department department = departmentRepository.findById(request.getDeptId()).orElseThrow(() -> {
			logger.warn("Department with ID {} not found", request.getDeptId());
			return new AppException(ErrorCode.DEPARTMENT_NOT_FOUND,
					"Department with ID " + request.getDeptId() + " not found.");
		});

		// Kiểm tra và xử lý user
		User user;
		Optional<User> userOptional = userRepository.findByEmail(request.getUserEmail());
		if (userOptional.isPresent()) {
			user = userOptional.get();
			Role teacherRole = roleRepository.findByRoleName("TEACHER")
					.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
			if (!user.getRole().getRoleName().equals("TEACHER")) {
				throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS,
						"User with email '" + request.getUserEmail() + "' already exists with a different role.");
			}
		} else {
			user = new User();
			user.setUsername(request.getTeacherName());
			user.setPassword(jwtService.encodePassword("password123"));
			user.setEmail(request.getUserEmail());

			Role roleEntity = roleRepository.findByRoleName("TEACHER")
					.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
			user.setRole(roleEntity);

			user = userRepository.save(user);
			logger.info("Created user with ID {} for teacher", user.getId());
		}

		Teacher teacher = new Teacher();
		String teacherCode = generateTeacherCode(user.getId());
		String teacherEmail = generateEmail(user.getUsername(), teacherCode);
		teacher.setTeacherCode(teacherCode);
		teacher.setTeacherName(request.getTeacherName());
		teacher.setTeacherDateOfBirth(request.getTeacherDateOfBirth());
		teacher.setTeacherGender(request.getTeacherGender());
		teacher.setTeacherAddress(request.getTeacherAddress());
		teacher.setTeacherPhoneNumber(request.getTeacherPhoneNumber());
		teacher.setTeacherEmail(teacherEmail);
		teacher.setUser(user);
		teacher.setDepartment(department);

		Teacher savedTeacher = teacherRepository.save(teacher);
		logger.info("Created teacher with ID {} and teacherCode {}", savedTeacher.getId(),
				savedTeacher.getTeacherCode());
		return TeacherMapper.toTeacherResponse(savedTeacher);
	}

	@Transactional
	public TeacherResponse updateTeacher(Long id, TeacherUpdateRequest request) {
		Teacher teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));

		teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber()).ifPresent(existing -> {
			if (!existing.getId().equals(id)) {
				logger.warn("Teacher with phone number {} already exists", request.getTeacherPhoneNumber());
				throw new AppException(ErrorCode.TEACHER_ALLREADY_EXISTS,
						"Teacher with phone number '" + request.getTeacherPhoneNumber() + "' already exists.");
			}
		});

		if (request.getDeptId() != null) {
			Department department = departmentRepository.findById(request.getDeptId()).orElseThrow(() -> {
				logger.warn("Department with ID {} not found", request.getDeptId());
				return new AppException(ErrorCode.DEPARTMENT_NOT_FOUND,
						"Department with ID " + request.getDeptId() + " not found.");
			});
			teacher.setDepartment(department);
		}

		if (request.getTeacherName() != null)
			teacher.setTeacherName(request.getTeacherName());
		if (request.getTeacherDateOfBirth() != null)
			teacher.setTeacherDateOfBirth(request.getTeacherDateOfBirth());
		if (request.getTeacherGender() != null)
			teacher.setTeacherGender(request.getTeacherGender());
		if (request.getTeacherAddress() != null)
			teacher.setTeacherAddress(request.getTeacherAddress());
		if (request.getTeacherPhoneNumber() != null)
			teacher.setTeacherPhoneNumber(request.getTeacherPhoneNumber());
		if (request.getTeacherEmail() != null)
			teacher.setTeacherEmail(request.getTeacherEmail());

		Teacher updatedTeacher = teacherRepository.save(teacher);
		logger.info("Updated teacher with ID {}", updatedTeacher.getId());
		return TeacherMapper.toTeacherResponse(updatedTeacher);
	}

	@Transactional
	public void deleteTeacher(Long id) {
		Teacher teacher = teacherRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.TEACHER_NOT_FOUND));
		teacherRepository.delete(teacher);
		logger.info("Deleted teacher with ID {}", id);
	}

	private String generateTeacherCode(Long id) {
		int currentYear = java.time.Year.now().getValue();
		return "GV" + currentYear + String.format("%05d", id);
	}

	private String generateEmail(String name, String teacherCode) {
		String normalized = name.trim().toLowerCase().replaceAll("[àáạảãâầấậẩẫăằắặẳẵ]", "a")
				.replaceAll("[èéẹẻẽêềếệểễ]", "e").replaceAll("[ìíịỉĩ]", "i").replaceAll("[òóọỏõôồốộổỗơờớợởỡ]", "o")
				.replaceAll("[ùúụủũưừứựửữ]", "u").replaceAll("[ỳýỵỷỹ]", "y").replaceAll("đ", "d")
				.replaceAll("[^a-z0-9]", "");
		return normalized + "_" + teacherCode.toLowerCase() + "@university.edu.vn";
	}

	
	public ByteArrayInputStream exportTeachersToExcel() throws IOException {
        List<Teacher> teachers = teacherRepository.findAll();

        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Teachers");

            // Header row
            String[] headers = {
                "ID", "Mã GV", "Tên GV", "Ngày sinh", "Giới tính", "Số ĐT", "Địa chỉ", "Email", "Khoa"
            };

            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Data rows
            int rowIdx = 1;
            for (Teacher teacher : teachers) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(teacher.getId());
                row.createCell(1).setCellValue(teacher.getTeacherCode());
                row.createCell(2).setCellValue(teacher.getTeacherName());

                if (teacher.getTeacherDateOfBirth() != null) {
                    Cell dateCell = row.createCell(3);
                    dateCell.setCellValue(teacher.getTeacherDateOfBirth());
                    CellStyle dateStyle = workbook.createCellStyle();
                    CreationHelper createHelper = workbook.getCreationHelper();
                    dateStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
                    dateCell.setCellStyle(dateStyle);
                } else {
                    row.createCell(3).setCellValue("");
                }

                row.createCell(4).setCellValue(teacher.getTeacherGender() != null ? teacher.getTeacherGender().name() : "");
                row.createCell(5).setCellValue(teacher.getTeacherPhoneNumber() != null ? teacher.getTeacherPhoneNumber() : "");
                row.createCell(6).setCellValue(teacher.getTeacherAddress() != null ? teacher.getTeacherAddress() : "");
                row.createCell(7).setCellValue(teacher.getTeacherEmail() != null ? teacher.getTeacherEmail() : "");
                row.createCell(8).setCellValue(teacher.getDepartment() != null ? teacher.getDepartment().getDeptName() : "");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
