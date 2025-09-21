package com.example.task_manager.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
