package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.studentApp.entity.ClassGroup;

public interface ClassGroupRepository extends JpaRepository<ClassGroup, Long> {

}
