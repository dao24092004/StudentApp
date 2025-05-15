package com.studentApp.dto.request;

import lombok.Data;

@Data
public class NotificationRequest {
	private Long senderId; // ID của người gửi (admin)
	private String title;
	private String content;
}