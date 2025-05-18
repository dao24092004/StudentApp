package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Grade;
import com.studentApp.entity.Student;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {

	// Tìm danh sách Grade theo Student
	@Query("SELECT g FROM Grade g WHERE g.student = :student")
	List<Grade> findByStudent(Student student);

	// Tùy chọn: Tìm danh sách Grade theo studentId (nếu cần truy vấn trực tiếp bằng
	// ID)
	@Query("SELECT g FROM Grade g JOIN g.student s WHERE s.id = :studentId")
	List<Grade> findByStudentId(Long studentId);
}