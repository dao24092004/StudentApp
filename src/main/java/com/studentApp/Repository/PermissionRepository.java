package com.studentApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.Entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Permission findByPermissionName(String permissionName);
}