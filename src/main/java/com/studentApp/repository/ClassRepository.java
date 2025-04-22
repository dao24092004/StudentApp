package com.studentApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {

	@Query("SELECT c FROM Class c WHERE c.subject.semester.id = :semesterId AND c.teacher IS NOT NULL")
	List<Class> findBySemesterIdAndTeacherIdIsNotNull(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c WHERE c.subject.semester.id = :semesterId AND c.teacher IS NULL")
	List<Class> findBySemesterIdAndTeacherIdIsNull(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c WHERE c.classGroup.semester.id = :semesterId")
	List<Class> findBySemesterId(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c WHERE c.classGroup.semester.id = :semesterId AND c.teacher IS NULL")
	List<Class> findUnassignedBySemesterId(@Param("semesterId") Long semesterId);
}