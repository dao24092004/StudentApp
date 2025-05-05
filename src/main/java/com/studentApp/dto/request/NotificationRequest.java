package com.studentApp.dto.request;

import java.time.LocalDateTime;

import com.studentApp.entity.Notification.NotificationType;

import lombok.Data;

@Data
public class NotificationRequest {
    private String title;
    private Long senderId;
    private NotificationType notificationType;
    private LocalDateTime sentAt;
}
