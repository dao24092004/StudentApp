package com.studentApp.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificationResponse {
	private Long id;
	private String title;
	private String content;
	private LocalDateTime sentAt;
	private boolean isRead;
}