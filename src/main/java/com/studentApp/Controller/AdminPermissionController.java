package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.AssignPermissionRequest;
import com.studentApp.dto.request.PermissionCreationRequest;
import com.studentApp.dto.request.PermissionUpdateRequest;
import com.studentApp.dto.request.RevokePermissionRequest;
import com.studentApp.dto.response.PermissionResponse;
import com.studentApp.service.PermissionService;

@RestController
@RequestMapping("/admin/permissions")
public class AdminPermissionController {

	@Autowired
	private PermissionService permissionService;

	// Thêm quyền mới
	@PostMapping
	@PreAuthorize("hasAuthority('PERMISSION_CREATE')")
	public ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest request) {
		PermissionResponse response = permissionService.createPermission(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/view/getall")
	@PreAuthorize("hasAuthority('PERMISSION_VIEW')")
	public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
		List<PermissionResponse> response = permissionService.getAllPermissions();
		return ResponseEntity.ok(response);
	}

	// Sửa quyền
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
	public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id,
			@RequestBody PermissionUpdateRequest request) {
		PermissionResponse response = permissionService.updatePermission(id, request);
		return ResponseEntity.ok(response);
	}

	// Xóa quyền
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISSION_DELETE')")
	public ResponseEntity<String> deletePermission(@PathVariable Long id) {
		permissionService.deletePermission(id);
		return ResponseEntity.ok("Permission deleted successfully");
	}

	// Gán quyền cho vai trò
	@PostMapping("/assign")
	@PreAuthorize("hasAuthority('PERMISSION_ASSIGN')")
	public ResponseEntity<String> assignPermissionToRole(@RequestBody AssignPermissionRequest request) {
		permissionService.assignPermissionToRole(request);
		return ResponseEntity.ok("Permission assigned to role successfully");
	}

	// Xóa quyền khỏi vai trò
	@DeleteMapping("/revoke")
	@PreAuthorize("hasAuthority('PERMISSION_REVOKE')")
	public ResponseEntity<String> revokePermissionFromRole(@RequestBody RevokePermissionRequest request) {
		permissionService.revokePermissionFromRole(request);
		return ResponseEntity.ok("Permission revoked from role successfully");
	}
}