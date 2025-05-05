package com.studentApp.dto.response;

import java.time.LocalDateTime;

import com.studentApp.entity.Notification;

import lombok.Data;

@Data
public class NotificationResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime sentAt;
    private Notification.NotificationType notificationType;
}
