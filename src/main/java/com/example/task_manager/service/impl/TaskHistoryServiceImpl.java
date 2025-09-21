package com.example.task_manager.service.impl;

import com.example.task_manager.dto.TaskHistoryDTO;
import com.example.task_manager.mapper.TaskHistoryMapper;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.TaskHistory;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.TaskHistoryRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskHistoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskHistoryServiceImpl implements TaskHistoryService {

    private final TaskHistoryRepository historyRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskHistoryServiceImpl(TaskHistoryRepository historyRepository,
                                  TaskRepository taskRepository,
                                  UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void recordChange(Long taskId, Long userId, String fieldChanged, String oldValue, String newValue) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User user = null;
        if (userId != null) {
            user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

        TaskHistory history = new TaskHistory();
        history.setTask(task);
        history.setUser(user);
        history.setFieldChanged(fieldChanged);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);
        history.setChangedAt(LocalDateTime.now());

        historyRepository.save(history);
    }

    @Override
    public List<TaskHistoryDTO> getTaskHistory(Long taskId) {
        return historyRepository.findByTaskId(taskId).stream()
                .map(TaskHistoryMapper::toDTO)
                .collect(Collectors.toList());
    }
}
