package com.studentApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.MajorRequestDTO;
import com.studentApp.dto.response.MajorResponseDTO;
import com.studentApp.service.MajorService;

@RestController
@RequestMapping("/admin/majors")
public class AdminMajorController {

	@Autowired
	private MajorService majorService;

	@PostMapping
	@PreAuthorize("hasAuthority('PERMISSION_CREATE')")
	public ResponseEntity<MajorResponseDTO> createMajor(@RequestBody MajorRequestDTO request) {
		MajorResponseDTO response = majorService.createMajor(request);
		return ResponseEntity.ok(response);
	}
}