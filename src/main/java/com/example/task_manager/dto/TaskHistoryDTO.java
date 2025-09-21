package com.example.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskHistoryDTO {
    private Long id;
    private Long taskId;
    private Long userId;
    private String fieldChanged;
    private String oldValue;
    private String newValue;
    private LocalDateTime changedAt;
}
