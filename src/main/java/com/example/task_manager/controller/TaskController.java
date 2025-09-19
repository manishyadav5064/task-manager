package com.example.task_manager.controller;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.service.TaskService;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/tasks")
public class TaskController {

    private TaskService taskService;

    @PostMapping
    public RequestEntity<TaskRequest> createTask(@RequestBody TaskRequest taskRequest) {
        return null;
    }

    @GetMapping("{id}")
    public void getTaskById(@PathVariable("id") Long taskId) {

    }

    @GetMapping
    public void getAllTasks() {

    }

    @PutMapping("{id}")
    public void updateTask(@PathVariable("id") Long taskId, @RequestBody TaskRequest taskRequest) {

    }

    @DeleteMapping("{id}")
    public void deleteTask(@PathVariable("id") Long taskId) {

    }
}
