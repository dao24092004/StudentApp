package com.studentApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.studentApp.entity.RolePermission;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermission.RolePermissionId> {

	@Query("SELECT rp FROM RolePermission rp " + "JOIN Role r ON rp.roleId = r.id "
			+ "JOIN Permission p ON rp.permissionId = p.id "
			+ "WHERE r.roleName = :roleName AND p.permissionName = :permissionName")
	RolePermission findByRoleNameAndPermissionName(@Param("roleName") String roleName,
			@Param("permissionName") String permissionName);

	@Modifying
	@Query("DELETE FROM RolePermission rp WHERE rp.roleId = :roleId AND rp.permissionId = :permissionId")
	void deleteByRoleIdAndPermissionId(Long roleId, Long permissionId);

	@Modifying
	@Query("DELETE FROM RolePermission rp WHERE rp.permissionId = :permissionId")
	void deleteByPermissionId(Long permissionId);
}