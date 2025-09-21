package com.example.task_manager.scheduler;

import com.example.task_manager.model.Task;
import com.example.task_manager.service.EmailService;
import com.example.task_manager.service.TaskService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TaskReminderScheduler {

    private final TaskService taskService;
    private final EmailService emailService;

    public TaskReminderScheduler(TaskService taskService, EmailService emailService) {
        this.taskService = taskService;
        this.emailService = emailService;
    }

    // Run every day at 8 AM
    @Scheduled(cron = "0 0 8 * * ?")
    public void sendTaskReminders() {
        List<Task> tasks = taskService.getAllTasksEntities();
        LocalDate today = LocalDate.now();

        for (Task task : tasks) {
            if (task.getUser() != null && task.getDueDate() != null && !task.getIsCompleted()) {
                LocalDate dueDate = task.getDueDate();

                if (dueDate.equals(today.plusDays(1))) {
                    // Task due tomorrow
                    emailService.sendEmail(
                            task.getUser().getEmail(),
                            "Task Reminder: " + task.getTitle(),
                            "Your task \"" + task.getTitle() + "\" is due tomorrow (" + dueDate + ")."
                    );
                }

                if (dueDate.isBefore(today)) {
                    // Task overdue
                    emailService.sendEmail(
                            task.getUser().getEmail(),
                            "Task Overdue: " + task.getTitle(),
                            "Your task \"" + task.getTitle() + "\" was due on " + dueDate + " and is now overdue."
                    );
                }
            }
        }
    }
}
