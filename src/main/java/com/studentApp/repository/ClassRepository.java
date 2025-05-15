package com.studentApp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.studentApp.entity.Class;

public interface ClassRepository extends JpaRepository<Class, Long> {

	List<Class> findBySemesterIdAndTeacherIsNotNull(Long semesterId);

	@Query("SELECT c FROM Class c " + "JOIN CurriculumDetail cd ON cd.subject.id = c.subject.id "
			+ "WHERE cd.semester.id = :semesterId AND c.teacher IS NULL")
	List<Class> findBySemesterIdAndTeacherIdIsNull(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c WHERE c.classGroup.semester.id = :semesterId")
	List<Class> findBySemesterId(@Param("semesterId") Long semesterId);

	@Query("SELECT c FROM Class c WHERE c.classGroup.semester.id = :semesterId AND c.teacher IS NULL")
	List<Class> findUnassignedBySemesterId(@Param("semesterId") Long semesterId);

	Optional<Class> findByClassCode(String classCode);

}