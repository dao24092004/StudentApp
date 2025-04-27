package com.studentApp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.service.DataGeneratorService;

@RestController
@RequestMapping("/api/classes/data-generator")
public class DataGeneratorController {

	@Autowired
	private DataGeneratorService dataGeneratorService;

	@PostMapping("/generate")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> generateSampleData() {
		dataGeneratorService.generateSampleData();
		return ResponseEntity.ok(Map.of("status", "success", "message", "Sample data generated successfully"));
	}

}