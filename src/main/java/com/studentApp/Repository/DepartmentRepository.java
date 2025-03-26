package com.studentApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.Entity.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
	Department findByDeptName(String departmentName);
}