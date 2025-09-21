package com.example.task_manager.service.impl;

import com.example.task_manager.model.Notification;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.NotificationRepository;
import com.example.task_manager.service.NotificationService;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void notifyUser(User user, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notificationRepository.save(notification);

        // Optional: print or log
        System.out.println("Notification for " + user.getUsername() + ": " + message);
    }
}
