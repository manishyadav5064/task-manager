package com.example.task_manager.mapper;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.model.Task;

public class TaskMapper {

    // Convert Task entity to TaskDTO
    public static TaskDTO mapToTaskDTO(Task task) {
        Long userId = (task.getUser() != null) ? task.getUser().getId() : null;

        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getIsCompleted(),
                userId
        );
    }

    // Convert TaskDTO to Task entity
    public static Task mapToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setId(taskDTO.getId());
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setDueDate(taskDTO.getDueDate());
        task.setIsCompleted(taskDTO.getIsCompleted());

        // User will be set separately in service/controller
        task.setUser(null);

        return task;
    }
}
