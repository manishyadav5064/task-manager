package com.example.task_manager.service.impl;

import com.example.task_manager.dto.UserDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.UserMapper;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = UserMapper.mapToUser(userDTO);
        User savedUser = userRepository.save(user);
        return UserMapper.mapToUserDTO(savedUser);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.mapToUserDTO(user);
    }
}
