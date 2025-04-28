package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.ClassGroup;

@Repository
public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {
	ClassGroup findByGroupName(String groupName);

	Optional<ClassGroup> findByGroupCode(String groupCode);

	boolean existsByGroupName(String groupName);
}