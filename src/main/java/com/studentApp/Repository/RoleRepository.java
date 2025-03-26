package com.studentApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.studentApp.Entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRoleName(String roleName);

	@Query("SELECT r FROM Role r JOIN FETCH r.permissions WHERE r.roleName = :roleName")
	Optional<Role> findByRoleNameWithPermissions(String roleName);
}