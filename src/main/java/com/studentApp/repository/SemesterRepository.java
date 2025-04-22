package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentApp.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
}