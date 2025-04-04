package com.studentApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
	Optional<Permission> findByPermissionName(String permissionName);
}