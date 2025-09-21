package com.example.task_manager.dto;

import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be less than 100 characters")
    private String title;

    @Size(max = 500, message = "Description must be less than 500 characters")
    private String description;

    @NotNull(message = "Due date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")  // ensures correct JSON format
    private LocalDate dueDate;

    private Boolean isCompleted;

    @NotNull(message = "Status cannot be null")
    private TaskStatus status = TaskStatus.TODO;

    @NotNull(message = "Priority cannot be null")
    private TaskPriority priority = TaskPriority.MEDIUM;

    private Long userId; // Optional: ID of the assigned user

    // Getters and setters
}
