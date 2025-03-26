package com.studentApp.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.studentApp.dto.request.AssignPermissionRequest;
import com.studentApp.dto.request.PermissionCreationRequest;
import com.studentApp.dto.request.PermissionUpdateRequest;
import com.studentApp.dto.request.RevokePermissionRequest;
import com.studentApp.dto.response.PermissionResponse;
import com.studentApp.entity.Permission;
import com.studentApp.entity.Role;
import com.studentApp.entity.RolePermission;
import com.studentApp.enums.ErrorCode;
import com.studentApp.exception.AppException;
import com.studentApp.repository.PermissionRepository;
import com.studentApp.repository.RolePermissionRepository;
import com.studentApp.repository.RoleRepository;

@Service
public class PermissionService {

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private RolePermissionRepository rolePermissionRepository;

	@Transactional
	public PermissionResponse createPermission(PermissionCreationRequest request) {
		if (permissionRepository.findByPermissionName(request.getPermissionName()) != null) {
			throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
		}

		Permission permission = new Permission();
		permission.setPermissionName(request.getPermissionName());
		permission.setDescription(request.getDescription());
		permission.setCreatedAt(LocalDateTime.now());
		permission.setUpdatedAt(LocalDateTime.now());

		permission = permissionRepository.save(permission);
		return toPermissionResponse(permission);
	}

	@Transactional
	public PermissionResponse updatePermission(Long id, PermissionUpdateRequest request) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

		Permission existingPermission = permissionRepository.findByPermissionName(request.getPermissionName())
				.orElse(null);

		if (existingPermission != null && !existingPermission.getId().equals(id)) {
			throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS);
		}

		permission.setPermissionName(request.getPermissionName());
		permission.setDescription(request.getDescription());
		permission.setUpdatedAt(LocalDateTime.now());

		permission = permissionRepository.save(permission);
		return toPermissionResponse(permission);
	}

	@Transactional
	public void deletePermission(Long id) {
		Permission permission = permissionRepository.findById(id)
				.orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

		rolePermissionRepository.deleteByPermissionId(id);
		permissionRepository.delete(permission);
	}

	@Transactional
	public void assignPermissionToRole(AssignPermissionRequest request) {
		Role role = roleRepository.findByRoleName(request.getRoleName())
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

		Permission permission = permissionRepository.findByPermissionName(request.getPermissionName())
				.orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

		RolePermission existingAssignment = rolePermissionRepository
				.findByRoleNameAndPermissionName(request.getRoleName(), request.getPermissionName());
		if (existingAssignment != null) {
			throw new AppException(ErrorCode.PERMISSION_ALREADY_ASSIGNED);
		}

		RolePermission rolePermission = new RolePermission();
		rolePermission.setRoleId(role.getId());
		rolePermission.setPermissionId(permission.getId());
		rolePermission.setCreatedAt(LocalDateTime.now());
		rolePermission.setUpdatedAt(LocalDateTime.now());

		rolePermissionRepository.save(rolePermission);
	}

	@Transactional
	public void revokePermissionFromRole(RevokePermissionRequest request) {
		Role role = roleRepository.findByRoleName(request.getRoleName())
				.orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

		Permission permission = permissionRepository.findByPermissionName(request.getPermissionName())
				.orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

		RolePermission existingAssignment = rolePermissionRepository
				.findByRoleNameAndPermissionName(request.getRoleName(), request.getPermissionName());
		if (existingAssignment == null) {
			throw new AppException(ErrorCode.PERMISSION_NOT_ASSIGNED);
		}

		rolePermissionRepository.deleteByRoleIdAndPermissionId(role.getId(), permission.getId());
	}

	private PermissionResponse toPermissionResponse(Permission permission) {
		PermissionResponse response = new PermissionResponse();
		response.setId(permission.getId());
		response.setPermissionName(permission.getPermissionName());
		response.setDescription(permission.getDescription());
		response.setCreatedAt(permission.getCreatedAt());
		response.setUpdatedAt(permission.getUpdatedAt());
		return response;
	}
}