package com.example.task_manager.service;

import com.example.task_manager.model.User;

public interface NotificationService {
    void notifyUser(User user, String message);
}
