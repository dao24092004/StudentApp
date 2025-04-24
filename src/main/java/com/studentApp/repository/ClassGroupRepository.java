package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.ClassGroup;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
	ClassGroup findByGroupName(String groupName);

	boolean existsByGroupName(String groupName);
}