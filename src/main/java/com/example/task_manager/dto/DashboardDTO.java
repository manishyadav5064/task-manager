package com.example.task_manager.dto;

import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    private Long totalTasks;
    private Map<TaskStatus, Long> tasksByStatus;
    private Map<TaskPriority, Long> tasksByPriority;
    private Map<String, Long> tasksByLabel; // Label name -> count
    private Map<String, Long> tasksByUser;  // User name -> count
    private long dueToday;
    private long overdue;
    private List<TaskDTO> upcomingTasks; // next 5 tasks by due date
}
