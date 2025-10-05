package com.example.task_manager.controller;

import com.example.task_manager.dto.DashboardDTO;
import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.dto.TaskHistoryDTO;
import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import com.example.task_manager.service.TaskHistoryService;
import com.example.task_manager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskHistoryService taskHistoryService;

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {

        TaskDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTask);
    }

    // Assign an existing task to a user
    @PutMapping("/assign/{taskId}")
    public ResponseEntity<TaskDTO> assignTaskToUser(@PathVariable Long taskId,
                                                    @RequestParam Long userId) {

        TaskDTO updatedTask = taskService.assignTaskToUser(taskId, userId);
        return ResponseEntity.ok(updatedTask);
    }

    // Get all tasks
    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    // Get all tasks for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskDTO>> getTasksByUser(@PathVariable Long userId) {
        List<TaskDTO> tasks = taskService.getTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }

    // Get a task by id
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // Update a task
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable Long id, @Valid @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    // Delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> filterTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority) {

        List<TaskDTO> filteredTasks = taskService.filterTasks(status, priority);
        return ResponseEntity.ok(filteredTasks);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskDTO>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate) {

        List<TaskDTO> tasks = taskService.searchTasks(title, description, userId, dueDate);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/labels/{labelId}")
    public ResponseEntity<TaskDTO> addLabel(
            @PathVariable Long taskId,
            @PathVariable Long labelId) {

        TaskDTO updatedTask = taskService.addLabelToTask(taskId, labelId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{taskId}/labels/{labelId}")
    public ResponseEntity<TaskDTO> removeLabel(
            @PathVariable Long taskId,
            @PathVariable Long labelId) {

        TaskDTO updatedTask = taskService.removeLabelFromTask(taskId, labelId);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDTO> getDashboard(
            @RequestParam(required = false) Long userId) {

        DashboardDTO dashboard = taskService.getDashboardStatistics(userId);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/{taskId}/history")
    public ResponseEntity<List<TaskHistoryDTO>> getTaskHistory(@PathVariable Long taskId) {
        List<TaskHistoryDTO> history = taskHistoryService.getTaskHistory(taskId);
        return ResponseEntity.ok(history);
    }

}
