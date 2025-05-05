package com.studentApp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentApp.dto.response.NotificationResponse;
import com.studentApp.service.NotificationService;

@RestController
@RequestMapping("/api/users")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/teacher")
	@PreAuthorize("hasAuthority('USER_VIEW')")
	public ResponseEntity<List<NotificationResponse>> getAllNotification() {
		return ResponseEntity.ok(notificationService.getAllNotification());
	}
}
