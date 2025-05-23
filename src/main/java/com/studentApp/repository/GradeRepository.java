package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Grade;

@Repository
public interface GradeRepository extends JpaRepository<Grade, Long> {
}
