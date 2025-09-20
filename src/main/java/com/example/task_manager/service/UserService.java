package com.example.task_manager.service;

import com.example.task_manager.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO user);

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);
}
