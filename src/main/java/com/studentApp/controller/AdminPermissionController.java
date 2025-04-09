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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/admin/permissions")
@Tag(name = "Admin Permissions", description = "APIs for managing permissions (admin only)")
public class AdminPermissionController {

	@Autowired
	private PermissionService permissionService;

	// Thêm quyền mới
	@PostMapping
	@PreAuthorize("hasAuthority('PERMISSION_CREATE')")
	@Operation(summary = "Create a new permission", description = "Create a new permission for the system")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permission created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionCreationRequest request) {
		PermissionResponse response = permissionService.createPermission(request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/view/getall")
	@PreAuthorize("hasAuthority('PERMISSION_VIEW')")
	@Operation(summary = "Get all permissions", description = "Retrieve a list of all permissions in the system")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permissions retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
		List<PermissionResponse> response = permissionService.getAllPermissions();
		return ResponseEntity.ok(response);
	}

	// Sửa quyền
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISSION_UPDATE')")
	@Operation(summary = "Update a permission", description = "Update an existing permission by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permission updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PermissionResponse.class))),
			@ApiResponse(responseCode = "404", description = "Permission not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id,
			@RequestBody PermissionUpdateRequest request) {
		PermissionResponse response = permissionService.updatePermission(id, request);
		return ResponseEntity.ok(response);
	}

	// Xóa quyền
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('PERMISSION_DELETE')")
	@Operation(summary = "Delete a permission", description = "Delete a permission by ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permission deleted successfully", content = @Content),
			@ApiResponse(responseCode = "404", description = "Permission not found", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<String> deletePermission(@PathVariable Long id) {
		permissionService.deletePermission(id);
		return ResponseEntity.ok("Permission deleted successfully");
	}

	// Gán quyền cho vai trò
	@PostMapping("/assign")
	@PreAuthorize("hasAuthority('PERMISSION_ASSIGN')")
	@Operation(summary = "Assign permission to role", description = "Assign a permission to a specific role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permission assigned successfully", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<String> assignPermissionToRole(@RequestBody AssignPermissionRequest request) {
		permissionService.assignPermissionToRole(request);
		return ResponseEntity.ok("Permission assigned to role successfully");
	}

	// Xóa quyền khỏi vai trò
	@DeleteMapping("/revoke")
	@PreAuthorize("hasAuthority('PERMISSION_REVOKE')")
	@Operation(summary = "Revoke permission from role", description = "Revoke a permission from a specific role")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Permission revoked successfully", content = @Content),
			@ApiResponse(responseCode = "403", description = "Access denied", content = @Content) })
	public ResponseEntity<String> revokePermissionFromRole(@RequestBody RevokePermissionRequest request) {
		permissionService.revokePermissionFromRole(request);
		return ResponseEntity.ok("Permission revoked from role successfully");
	}
}