package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Teacher;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
	Optional<Teacher> findByTeacherCode(String teacherCode);

	Optional<Teacher> findByTeacherEmail(String email);

	Optional<Teacher> findByTeacherPhoneNumber(String teacherPhoneNumber);

	Optional<Teacher> findByTeacherName(String teacherName);

}