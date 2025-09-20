package com.example.task_manager.controller;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // Create a general task (unassigned)
    @PostMapping("/create")
    public TaskDTO createTask(@Valid @RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO, null);
    }

    // Create task for a specific user
    @PostMapping
    public TaskDTO createTaskForUser(@Valid @RequestBody TaskDTO taskDTO, @RequestParam Long userId) {
        return taskService.createTask(taskDTO, userId);
    }

    // Assign an existing task to a user
    @PutMapping("/assign/{taskId}")
    public TaskDTO assignTaskToUser(@PathVariable Long taskId, @RequestParam Long userId) {
        return taskService.assignTaskToUser(taskId, userId);
    }

    // Get all tasks
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    // Get all tasks for a specific user
    @GetMapping("/user/{userId}")
    public List<TaskDTO> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId);
    }

    // Get a task by id
    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    // Update a task
    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        return taskService.updateTask(id, taskDTO);
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
