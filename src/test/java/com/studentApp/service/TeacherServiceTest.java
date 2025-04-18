package com.studentApp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.studentApp.dto.request.TeacherCreationRequest;
import com.studentApp.dto.response.TeacherResponse;
import com.studentApp.entity.Role;
import com.studentApp.entity.Teacher;
import com.studentApp.entity.User;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.RoleRepository;
import com.studentApp.repository.TeacherRepository;
import com.studentApp.repository.UserRepository;

class TeacherServiceTest {

	@Mock
	private TeacherRepository teacherRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleRepository roleRepository;

	@Mock
	private JwtService jwtService;

	@InjectMocks
	private TeacherService teacherService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void createTeacher_Success() {
		// Arrange: Chuẩn bị dữ liệu đầu vào
		TeacherCreationRequest request = TeacherCreationRequest.builder().teacherName("Nguyen Van A")
				.teacherDateOfBirth(Date.valueOf("1980-01-01")).teacherGender(Teacher.Gender.Male)
				.teacherAddress("123 Hanoi").teacherPhoneNumber("0912345678").userEmail("nguyenvana@example.com")
				.build();

		// Mock hành vi của teacherRepository.findByTeacherPhoneNumber
		when(teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber())).thenReturn(Optional.empty());

		// Mock hành vi của userRepository.findByEmail
		User existingUser = new User();
		existingUser.setId(1L);
		existingUser.setUsername("Nguyen Van A");
		existingUser.setEmail("nguyenvana@example.com");
		when(userRepository.findByEmail(request.getUserEmail())).thenReturn(existingUser);

		// Mock hành vi của roleRepository.findByRoleName
		Role teacherRole = new Role();
		teacherRole.setRoleName("TEACHER");
		when(roleRepository.findByRoleName("TEACHER")).thenReturn(Optional.of(teacherRole));

		// Mock hành vi của teacherRepository.save
		Teacher teacher = new Teacher();
		teacher.setId(1L); // Gán ID để tránh NullPointerException
		teacher.setTeacherName(request.getTeacherName());
		teacher.setTeacherEmail("nguyenvana_gv202500001@university.edu.vn");
		teacher.setUser(existingUser);
		when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

		// Act: Thực hiện phương thức cần kiểm thử
		TeacherResponse result = teacherService.createTeacher(request);

		// Assert: Kiểm tra kết quả
		assertNotNull(result);
		assertEquals("Nguyen Van A", result.getTeacherName());
		verify(teacherRepository, times(1)).save(any(Teacher.class));
	}

	@Test
	void createTeacher_NewUser_Success() {
		// Arrange: Chuẩn bị dữ liệu đầu vào
		TeacherCreationRequest request = TeacherCreationRequest.builder().teacherName("Nguyen Van B")
				.teacherDateOfBirth(Date.valueOf("1981-01-01")).teacherGender(Teacher.Gender.Male)
				.teacherAddress("456 Hanoi").teacherPhoneNumber("0987654321").userEmail("nguyenvanb@example.com")
				.build();

		// Mock hành vi của teacherRepository.findByTeacherPhoneNumber
		when(teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber())).thenReturn(Optional.empty());

		// Mock hành vi của userRepository.findByEmail (không tìm thấy user)
		when(userRepository.findByEmail(request.getUserEmail())).thenReturn(null);

		// Mock hành vi của roleRepository.findByRoleName
		Role teacherRole = new Role();
		teacherRole.setRoleName("TEACHER");
		when(roleRepository.findByRoleName("TEACHER")).thenReturn(Optional.of(teacherRole));

		// Mock hành vi của jwtService.encodePassword
		when(jwtService.encodePassword("password123")).thenReturn("encodedPassword");

		// Mock hành vi của userRepository.save
		User newUser = new User();
		newUser.setId(2L);
		newUser.setUsername("Nguyen Van B");
		newUser.setEmail("nguyenvanb@example.com");
		when(userRepository.save(any(User.class))).thenReturn(newUser);

		// Mock hành vi của teacherRepository.save
		Teacher teacher = new Teacher();
		teacher.setId(2L); // Gán ID để tránh NullPointerException
		teacher.setTeacherName(request.getTeacherName());
		teacher.setTeacherEmail("nguyen van b_gv202500002@university.edu.vn");
		teacher.setUser(newUser);
		when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
			Teacher t = invocation.getArgument(0);
			t.setId(2L); // Giả lập ID tự tăng
			return t;
		});

		// Act: Thực hiện phương thức cần kiểm thử
		TeacherResponse result = teacherService.createTeacher(request);

		// Assert: Kiểm tra kết quả
		assertNotNull(result);
		assertEquals("Nguyen Van B", result.getTeacherName());
		verify(userRepository, times(1)).save(any(User.class));
		verify(teacherRepository, times(2)).save(any(Teacher.class));
	}

	@Test
	void createTeacher_PhoneNumberExists_ThrowsException() {
		// Arrange: Chuẩn bị dữ liệu đầu vào
		TeacherCreationRequest request = TeacherCreationRequest.builder().teacherName("Nguyen Van A")
				.teacherDateOfBirth(Date.valueOf("1980-01-01")).teacherGender(Teacher.Gender.Male)
				.teacherAddress("123 Hanoi").teacherPhoneNumber("0912345678").userEmail("nguyenvana@example.com")
				.build();

		// Mock hành vi của teacherRepository.findByTeacherPhoneNumber
		when(teacherRepository.findByTeacherPhoneNumber(request.getTeacherPhoneNumber()))
				.thenReturn(Optional.of(new Teacher()));

		// Act & Assert: Kiểm tra ngoại lệ
		AppException exception = assertThrows(AppException.class, () -> teacherService.createTeacher(request));
		assertEquals(ErrorCode.TEACHER_ALLREADY_EXISTS, exception.getErrorCode());
	}
}