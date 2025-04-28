package com.studentApp.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentApp.entity.Semester;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
	Optional<Semester> findByStartDate(LocalDate startDate);

}