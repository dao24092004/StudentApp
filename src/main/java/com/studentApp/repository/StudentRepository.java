package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	// Kiểm tra sự tồn tại của studentCode
	boolean existsByStudentCode(String studentCode);

	// Kiểm tra sự tồn tại của phoneNumber
	boolean existsByPhoneNumber(String phoneNumber);

	// Tìm student theo phoneNumber (dùng để kiểm tra trùng lặp trong update)
	Optional<Student> findByPhoneNumber(String phoneNumber);

	// Kiểm tra sự tồn tại của user_id (đảm bảo một user chỉ liên kết với một
	// student)
	boolean existsByUserId(Long userId);
}