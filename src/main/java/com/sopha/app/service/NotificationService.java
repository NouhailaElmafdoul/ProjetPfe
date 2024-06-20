package com.sopha.app.service;

import com.sopha.app.models.Notification;
import com.sopha.app.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

	@Autowired
    private NotificationRepository notificationRepository;

    public void saveNotification(Notification notification) {
        notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsByUser(Long userId) {
        return notificationRepository.findByUserIdOrderByDateDesc(userId);
    }
    
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}
