package com.example.task_manager.controller;

import com.example.task_manager.dto.DashboardDTO;
import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.dto.TaskHistoryDTO;
import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import com.example.task_manager.service.TaskHistoryService;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskHistoryService taskHistoryService;

    public TaskController(TaskService taskService, TaskHistoryService taskHistoryService) {
        this.taskService = taskService;
        this.taskHistoryService = taskHistoryService;
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

    @GetMapping("/filter")
    public List<TaskDTO> filterTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority
    ) {
        return taskService.filterTasks(status, priority);
    }

    @GetMapping("/search")
    public List<TaskDTO> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate
    ) {
        return taskService.searchTasks(title, description, userId, dueDate);
    }

    @PutMapping("/{taskId}/labels/{labelId}")
    public TaskDTO addLabel(@PathVariable Long taskId, @PathVariable Long labelId) {
        return taskService.addLabelToTask(taskId, labelId);
    }

    @DeleteMapping("/{taskId}/labels/{labelId}")
    public TaskDTO removeLabel(@PathVariable Long taskId, @PathVariable Long labelId) {
        return taskService.removeLabelFromTask(taskId, labelId);
    }

    @GetMapping("/dashboard")
    public DashboardDTO getDashboard(@RequestParam(required = false) Long userId) {
        return taskService.getDashboardStatistics(userId);
    }

    @GetMapping("/{taskId}/history")
    public List<TaskHistoryDTO> getTaskHistory(@PathVariable Long taskId) {
        return taskHistoryService.getTaskHistory(taskId);
    }

}
