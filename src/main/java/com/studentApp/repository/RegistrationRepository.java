package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Registration;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {

	@Query("SELECT r FROM Registration r WHERE r.student.id = :studentId AND r.semester.id = :semesterId")
	List<Registration> findByStudentIdAndSemesterId(@Param("studentId") Long studentId,
			@Param("semesterId") Long semesterId);

	boolean existsByClassEntityIdAndStudentId(Long classId, Long studentId);

	@Query("SELECT r FROM Registration r WHERE r.classEntity.id = :classId")
	List<Registration> findByClassEntityId(@Param("classId") Long classId);
}