package com.example.task_manager.service;

import com.example.task_manager.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO task, Long userId);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByUser(Long userId);

    TaskDTO getTaskById(Long id);

    TaskDTO updateTask(Long id, TaskDTO task);

    TaskDTO assignTaskToUser(Long taskId, Long userId);

    void deleteTask(Long id);
}
