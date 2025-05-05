package com.studentApp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.studentApp.dto.response.NotificationResponse;
import com.studentApp.entity.Notification;
import com.studentApp.mapper.NotificationMapper;
import com.studentApp.repository.NotificationRespository;

@Service
public class NotificationService {

    @Autowired
    private NotificationRespository notificationRespository;

    public List<NotificationResponse> getAllNotification(){
        List<Notification> notifications = notificationRespository.findAll();
        List<NotificationResponse> responses = new ArrayList<>();
        for(Notification notification : notifications){
            responses.add(NotificationMapper.toNotificationResponse(notification));
        }
        return responses;
    }
}
