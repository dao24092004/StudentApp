package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Registration;

// Repository để truy vấn bảng tbl_registration
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
	// Tìm danh sách đăng ký theo student_id và semester_id
	List<Registration> findByStudentIdAndSemesterId(Long studentId, Long semesterId);

	// Tìm đăng ký theo student_id
	@Query("SELECT r FROM Registration r WHERE r.student.id = :studentId")
	List<Registration> findByStudentId(@Param("studentId") Long studentId);
}