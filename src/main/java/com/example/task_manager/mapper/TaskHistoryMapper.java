package com.example.task_manager.mapper;

import com.example.task_manager.dto.TaskHistoryDTO;
import com.example.task_manager.model.TaskHistory;

public class TaskHistoryMapper {

    public static TaskHistoryDTO toDTO(TaskHistory history) {
        TaskHistoryDTO dto = new TaskHistoryDTO();
        dto.setId(history.getId());
        dto.setTaskId(history.getTask().getId());
        dto.setUserId(history.getUser() != null ? history.getUser().getId() : null);
        dto.setFieldChanged(history.getFieldChanged());
        dto.setOldValue(history.getOldValue());
        dto.setNewValue(history.getNewValue());
        dto.setChangedAt(history.getChangedAt());
        return dto;
    }
}
