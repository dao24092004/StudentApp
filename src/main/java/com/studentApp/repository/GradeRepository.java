package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.ClassEntity;
import com.studentApp.entity.Grade;
import com.studentApp.entity.Student;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
    boolean existsByStudentAndClassEntity(Student student, ClassEntity classEntity);
}
