package com.studentApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.dto.response.UserProfileResponse;
import com.studentApp.entity.Student;
import com.studentApp.entity.User;
import com.studentApp.repository.StudentRepository;
import com.studentApp.repository.UserRepository;

@Service
public class UserProfileService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StudentRepository studentRepository;

	public UserProfileResponse getUserProfile(String username) {
		// Truy vấn User từ database
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new RuntimeException("User not found");
		}

		// Truy vấn Student từ database
		Student student = studentRepository.findByUserId(user.getId())
				.orElseThrow(() -> new RuntimeException("Student not found"));

		// Tạo UserProfileResponse
		UserProfileResponse response = new UserProfileResponse();
		response.setId(user.getId());
		response.setUsername(student.getStudentName());
		response.setEmail(user.getEmail());
		response.setRole(user.getRole());
		response.setAvatarUrl(user.getAvatarUrl());
		response.setCreatedAt(user.getCreatedAt() != null ? user.getCreatedAt().toString() : null);
		response.setUpdatedAt(user.getUpdatedAt() != null ? user.getUpdatedAt().toString() : null);
		response.setCredentialsNonExpired(user.isCredentialsNonExpired());
		response.setAccountNonExpired(user.isAccountNonExpired());
		response.setAccountNonLocked(user.isAccountNonLocked());

		// Lấy thông tin từ Student
		response.setStudentId(student.getId());
		response.setStudentCode(student.getStudentCode());
		response.setStatus(" Đang học ");
		response.setGender(student.getGender() != null ? student.getGender().toString() : null);
		response.setBirthDate(student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : null);
		response.setAddress(student.getAddress());
		response.setHometown("Hà nội");
		response.setEthnicity("Kinh");
		response.setReligion("Không");
		response.setCourse("Công nghệ thông tin");
		response.setPhone(student.getPhoneNumber());
		response.setJob("Sinh viên");

		// Lấy thông tin từ Major và ClassGroup
		response.setMajor(student.getMajor() != null ? student.getMajor().getMajorName() : null);
		response.setDepartment(student.getMajor() != null ? student.getMajor().getMajorName() : null);
		response.setClassName(student.getClassGroup() != null ? student.getClassGroup().getGroupName() : null);
		response.setClassCode(student.getClassGroup() != null ? student.getClassGroup().getGroupCode() : null);

		return response;
	}
}