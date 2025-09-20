package com.example.task_manager.mapper;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.model.User;

public class UserMapper {

    // Convert User entity to DTO (for responses)
    public static UserDTO mapToUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null // Never expose password in DTO responses
        );
    }

    // Convert DTO to User entity (for creation / update)
    public static User mapToUser(UserDTO userDTO) {
        if (userDTO == null) return null;
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword()); // Password is only set when creating/updating user
        return user;
    }
}
