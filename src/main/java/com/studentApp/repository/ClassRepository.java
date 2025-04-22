package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.studentApp.entity.Class;

import io.lettuce.core.dynamic.annotation.Param;

// Repository để truy vấn bảng tbl_class
public interface ClassRepository extends JpaRepository<Class, Long> {
	@Query("SELECT c FROM Class c WHERE c.semesterId = :semesterId AND c.teacherId IS NULL")
	List<Class> findBySemesterIdAndTeacherIdIsNull(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c JOIN Subject s ON c.subjectId = s.id WHERE s.semesterId = :semesterId AND c.teacherId IS NOT NULL")
	List<Class> findBySemesterIdAndTeacherIdNotNull(@Param("semesterId") Long semesterId);

	// Tìm tất cả lớp học theo semester_id
	@Query("SELECT c FROM Class c WHERE c.semester.id = :semesterId")
	List<Class> findBySemesterId(@Param("semesterId") Long semesterId);

	// Tìm lớp học theo subject_id
	@Query("SELECT c FROM Class c WHERE c.subject.id = :subjectId")
	List<Class> findBySubjectId(@Param("subjectId") Long subjectId);

	// Tìm lớp học chưa được phân công giáo viên
	@Query("SELECT c FROM Class c WHERE c.semester.id = :semesterId AND c.teacher IS NULL")
	List<Class> findUnassignedBySemesterId(@Param("semesterId") Long semesterId);

	// Tìm danh sách lớp học theo teacher_id
	List<Class> findByTeacherId(Long teacherId);
}