package com.example.task_manager.scheduler;

import com.example.task_manager.model.Task;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.service.NotificationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskDueDateScheduler {

    private final TaskService taskService;
    private final NotificationService notificationService;

    public TaskDueDateScheduler(TaskService taskService, NotificationService notificationService) {
        this.taskService = taskService;
        this.notificationService = notificationService;
    }

    // Run every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void checkDueTasks() {
        List<Task> tasks = taskService.getAllTasksEntities(); // return List<Task> (not DTO)
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (task.getDueDate() != null && task.getUser() != null) {
                if (task.getDueDate().isEqual(today)) {
                    notificationService.notifyUser(task.getUser(), "Task '" + task.getTitle() + "' is due today!");
                } else if (task.getDueDate().isBefore(today)) {
                    notificationService.notifyUser(task.getUser(), "Task '" + task.getTitle() + "' is overdue!");
                }
            }
        }
    }
}
