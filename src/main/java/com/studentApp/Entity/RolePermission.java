package com.studentApp.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tbl_role_permission")
@Data
@IdClass(RolePermission.RolePermissionId.class)
public class RolePermission {

	@Id
	@Column(name = "role_id", nullable = false)
	private Long roleId;

	@Id
	@Column(name = "permission_id", nullable = false)
	private Long permissionId;

	@Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime updatedAt;

	// Lớp IdClass để định nghĩa khóa chính composite
	public static class RolePermissionId implements Serializable {
		private Long roleId;
		private Long permissionId;

		public RolePermissionId() {
		}

		public RolePermissionId(Long roleId, Long permissionId) {
			this.roleId = roleId;
			this.permissionId = permissionId;
		}

		// Getters, setters, equals, hashCode
		public Long getRoleId() {
			return roleId;
		}

		public void setRoleId(Long roleId) {
			this.roleId = roleId;
		}

		public Long getPermissionId() {
			return permissionId;
		}

		public void setPermissionId(Long permissionId) {
			this.permissionId = permissionId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			RolePermissionId that = (RolePermissionId) o;
			return roleId.equals(that.roleId) && permissionId.equals(that.permissionId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(roleId, permissionId);
		}
	}
}