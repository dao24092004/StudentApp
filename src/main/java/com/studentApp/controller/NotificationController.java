package com.studentApp.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.request.NotificationRequest;
import com.studentApp.dto.response.NotificationResponse;
import com.studentApp.entity.Notification;
import com.studentApp.entity.User;
import com.studentApp.repository.UserRepository;
import com.studentApp.service.NotificationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@Autowired
	private UserRepository userRepository;

	private static final Logger log = LoggerFactory.getLogger(NotificationController.class);

	@PostMapping("/all-users")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	@Operation(summary = "Gửi thông báo đến tất cả người dùng")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Gửi thông báo thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập") })
	public ResponseEntity<Notification> sendToAllUsers(@RequestBody NotificationRequest request) {
		log.info("Sending notification to all users");
		User sender = userRepository.findById(request.getSenderId())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		Notification notification = notificationService.sendToAllUsers(request.getTitle(), request.getContent(),
				sender);
		return ResponseEntity.ok(notification);
	}

	@PostMapping("/all-teachers")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	@Operation(summary = "Gửi thông báo đến tất cả giáo viên")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Gửi thông báo thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập") })
	public ResponseEntity<Notification> sendToAllTeachers(@RequestBody NotificationRequest request) {
		log.info("Sending notification to all teachers");
		User sender = userRepository.findById(request.getSenderId())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		Notification notification = notificationService.sendToAllTeachers(request.getTitle(), request.getContent(),
				sender);
		return ResponseEntity.ok(notification);
	}

	@PostMapping("/all-students")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	@Operation(summary = "Gửi thông báo đến tất cả sinh viên")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Gửi thông báo thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập") })
	public ResponseEntity<Notification> sendToAllStudents(@RequestBody NotificationRequest request) {
		log.info("Sending notification to all students");
		User sender = userRepository.findById(request.getSenderId())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		Notification notification = notificationService.sendToAllStudents(request.getTitle(), request.getContent(),
				sender);
		return ResponseEntity.ok(notification);
	}

	@PostMapping("/individual/{recipientId}")
	@PreAuthorize("hasAuthority('CLASS_CREATE')")
	@Operation(summary = "Gửi thông báo đến một cá nhân cụ thể")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Gửi thông báo thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập") })
	public ResponseEntity<Notification> sendToIndividual(@PathVariable Long recipientId,
			@RequestBody NotificationRequest request) {
		log.info("Sending notification to individual with recipientId: {}", recipientId);
		User sender = userRepository.findById(request.getSenderId())
				.orElseThrow(() -> new RuntimeException("Sender not found"));
		Notification notification = notificationService.sendToIndividual(recipientId, request.getTitle(),
				request.getContent(), sender);
		return ResponseEntity.ok(notification);
	}

	@GetMapping("/student/{id}")
	@PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
	@Operation(summary = "Lấy danh sách thông báo của sinh viên theo studentId")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Lấy danh sách thông báo thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
			@ApiResponse(responseCode = "404", description = "Sinh viên không tồn tại") })
	public ResponseEntity<List<NotificationResponse>> getNotificationsByStudentId(@PathVariable Long id) {
		log.info("Getting notifications for studentId: {}", id);
		List<NotificationResponse> notifications = notificationService.getNotificationsByStudentId(id);
		log.info("Retrieved {} notifications for studentId: {}", notifications.size(), id);
		return ResponseEntity.ok(notifications);
	}

	@PostMapping("/mark-as-read/{notificationId}/{recipientId}")
	@PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
	@Operation(summary = "Đánh dấu thông báo là đã đọc")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Đánh dấu đã đọc thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
			@ApiResponse(responseCode = "404", description = "Thông báo không tồn tại") })
	public ResponseEntity<String> markAsRead(@PathVariable Long notificationId, @PathVariable Long recipientId) {
		log.info("Marking notification {} as read for recipientId: {}", notificationId, recipientId);
		notificationService.markAsRead(notificationId, recipientId);
		return ResponseEntity.ok("Notification marked as read");
	}

	@GetMapping("/unread-count/student/{id}")
	@PreAuthorize("hasAuthority('NOTIFICATION_VIEW')")
	@Operation(summary = "Đếm số lượng thông báo chưa đọc của sinh viên")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Lấy số lượng thông báo chưa đọc thành công"),
			@ApiResponse(responseCode = "403", description = "Không có quyền truy cập"),
			@ApiResponse(responseCode = "404", description = "Sinh viên không tồn tại") })
	public ResponseEntity<Long> countUnreadNotificationsByStudentId(@PathVariable Long id) {
		log.info("Counting unread notifications for studentId: {}", id);
		long unreadCount = notificationService.countUnreadNotificationsByStudentId(id);
		return ResponseEntity.ok(unreadCount);
	}
}