package com.example.task_manager.service;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskDTO createTask(TaskDTO task, Long userId);

    List<TaskDTO> getAllTasks();

    List<TaskDTO> getTasksByUser(Long userId);

    TaskDTO getTaskById(Long id);

    TaskDTO updateTask(Long id, TaskDTO task);

    TaskDTO assignTaskToUser(Long taskId, Long userId);

    void deleteTask(Long id);

    List<TaskDTO> filterTasks(TaskStatus status, TaskPriority priority);

    List<TaskDTO> searchTasks(String title, String description, Long userId, LocalDate dueDate);

    TaskDTO addLabelToTask(Long taskId, Long labelId);

    TaskDTO removeLabelFromTask(Long taskId, Long labelId);

    // this is required for scheduler
    List<Task> getAllTasksEntities();

}
