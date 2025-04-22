package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.TeacherSubjectRegistration;

public interface TeacherSubjectRegistrationRepository extends JpaRepository<TeacherSubjectRegistration, Long> {

	@Query("SELECT tsr FROM TeacherSubjectRegistration tsr WHERE tsr.semesterId = :semesterId")
	List<TeacherSubjectRegistration> findBySemesterId(@Param("semesterId") Long semesterId);
}