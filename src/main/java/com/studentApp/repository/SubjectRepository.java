package com.studentApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Subject;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

	@Query("SELECT s FROM Subject s WHERE s.semester.id = :semesterId AND s.deptId = :deptId")
	List<Subject> findBySemesterIdAndDeptId(@Param("semesterId") Long semesterId, @Param("deptId") Long deptId);

	@Query("SELECT s FROM Subject s JOIN TeacherSubjectRegistration tsr ON s.id = tsr.subject.id "
			+ "WHERE tsr.teacher.id = :teacherId AND tsr.semester.id = :semesterId")
	List<Subject> findByTeacherIdAndSemesterId(@Param("teacherId") Long teacherId,
			@Param("semesterId") Long semesterId);

	Optional<Subject> findBySubjectName(String subjectName);

}