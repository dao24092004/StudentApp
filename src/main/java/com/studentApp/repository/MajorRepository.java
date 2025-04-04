package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Major;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
	Major findByMajorCode(String majorCode);
}