package com.example.task_manager.service;

import com.example.task_manager.dto.TaskHistoryDTO;

import java.util.List;

public interface TaskHistoryService {
    void recordChange(Long taskId, Long userId, String fieldChanged, String oldValue, String newValue);

    List<TaskHistoryDTO> getTaskHistory(Long taskId);
}
