package com.example.task_manager.security.utils;

import com.example.task_manager.model.User;
import com.example.task_manager.model.enums.Role;
import com.example.task_manager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        return auth.getName();
    }

    public User getCurrentUser() {
        String username = getCurrentUsername();
        if (username == null) return null;
        return userRepository.findByUsername(username).orElse(null);
    }


    // Utility method
    public boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    public boolean isOwner(User taskOwner, User currentUser) {
        return taskOwner != null && taskOwner.getId().equals(currentUser.getId());
    }

    public User getCurrentUserEntity() {
        String username = getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found in DB"));
    }
}
