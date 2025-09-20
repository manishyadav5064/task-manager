package com.example.task_manager.service.impl;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.TaskMapper;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Task task = TaskMapper.mapToTask(taskDTO);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getTasks()
                .stream()
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskMapper.mapToTaskDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Update fields
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setIsCompleted(taskDTO.getIsCompleted());

        Task savedTask = taskRepository.save(existingTask);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public TaskDTO assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        } else {
            task.setUser(null);
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }
}
