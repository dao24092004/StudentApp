package com.studentApp.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.studentApp.service.ImportService;

@RestController
@RequestMapping("/api/classes/import")
public class ImportController {

	@Autowired
	private ImportService importService;

	@PostMapping("/curriculums")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importCurriculums(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importCurriculums(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Curriculums imported successfully"));
	}

	@PostMapping("/departments")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importDepartments(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importDepartments(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Departments imported successfully"));
	}

	@PostMapping("/majors")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importMajors(@RequestParam("file") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importMajors(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Majors imported successfully"));
	}

	@PostMapping("/class-groups")
	@PreAuthorize("hasAuthority('CLASS_GROUP_IMPORT')")
	public ResponseEntity<Map<String, String>> importClassGroups(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importClassGroups(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Class groups imported successfully"));
	}

	@PostMapping("/semesters")
	@PreAuthorize("hasAuthority('SEMESTER_IMPORT')")
	public ResponseEntity<Map<String, String>> importSemesters(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importSemesters(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Semesters imported successfully"));
	}

	@PostMapping("/teachers")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importTeachers(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importTeachers(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Teachers imported successfully"));
	}

	@PostMapping("/subjects")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importSubjects(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importSubjects(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Subjects imported successfully"));
	}

	@PostMapping("/classes")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importClasses(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importClasses(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Classes imported successfully"));
	}

	@PostMapping("/time-windows")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	public ResponseEntity<Map<String, String>> importTimeWindows(@RequestParam("file") MultipartFile file)
			throws Exception {
		if (file.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "File is empty"));
		}
		importService.importTimeWindows(file.getInputStream());
		return ResponseEntity.ok(Map.of("status", "success", "message", "Time windows imported successfully"));
	}
}