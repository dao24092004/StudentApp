package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	@Query("SELECT s FROM Subject s WHERE s.semesterId = :semesterId AND s.deptId = :deptId")
	List<Subject> findBySemesterIdAndDeptId(@Param("semesterId") Long semesterId, @Param("deptId") Long deptId);

	@Query("SELECT s FROM Subject s JOIN TeacherSubjectRegistration tsr ON s.id = tsr.subjectId WHERE tsr.teacherId = :teacherId AND tsr.semesterId = :semesterId")
	List<Subject> findByTeacherIdAndSemesterId(@Param("teacherId") Long teacherId,
			@Param("semesterId") Long semesterId);
}