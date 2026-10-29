package com.example.labres.service;

import com.example.labres.domain.Notification;
import com.example.labres.mapper.NotificationMapper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    public void notify(Long userId, String title, String content, Long relatedId) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setContent(content);
        n.setIsRead(false);
        n.setRelatedId(relatedId);
        notificationMapper.insert(n);
    }
}
