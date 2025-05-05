package com.studentApp.mapper;

import org.springframework.stereotype.Component;

import com.studentApp.dto.response.NotificationResponse;
import com.studentApp.entity.Notification;

@Component
public class NotificationMapper {

    public static NotificationResponse toNotificationResponse(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setSentAt(notification.getSentAt());
        return response;
    }
}
