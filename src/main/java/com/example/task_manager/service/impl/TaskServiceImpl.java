package com.example.task_manager.service.impl;

import com.example.task_manager.dto.TaskDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.TaskMapper;
import com.example.task_manager.model.Label;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import com.example.task_manager.repository.LabelRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;

    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository, LabelRepository labelRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.labelRepository = labelRepository;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO, Long userId) {
        Task task = TaskMapper.mapToTask(taskDTO);

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> getTasksByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getTasks()
                .stream()
                .map(TaskMapper::mapToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        return TaskMapper.mapToTaskDTO(task);
    }

    @Override
    public TaskDTO updateTask(Long id, TaskDTO taskDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Update fields
        existingTask.setTitle(taskDTO.getTitle());
        existingTask.setDescription(taskDTO.getDescription());
        existingTask.setDueDate(taskDTO.getDueDate());
        existingTask.setIsCompleted(taskDTO.getIsCompleted());

        Task savedTask = taskRepository.save(existingTask);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public TaskDTO assignTaskToUser(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        } else {
            task.setUser(null);
        }

        Task savedTask = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    @Override
    public List<TaskDTO> filterTasks(TaskStatus status, TaskPriority priority) {
        List<Task> tasks;

        if (status != null && priority != null) {
            tasks = taskRepository.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tasks = taskRepository.findByStatus(status);
        } else if (priority != null) {
            tasks = taskRepository.findByPriority(priority);
        } else {
            tasks = taskRepository.findAll();
        }

        return tasks.stream().map(TaskMapper::mapToTaskDTO).collect(Collectors.toList());
    }

    @Override
    public List<TaskDTO> searchTasks(String title, String description, Long userId, LocalDate dueDate) {
        return taskRepository.searchTasks(title, description, userId, dueDate)
                .stream()
                .map(TaskMapper::mapToTaskDTO)
                .toList();
    }

    @Override
    public TaskDTO addLabelToTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));

        task.getLabels().add(label);
        Task saved = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(saved);
    }

    @Override
    public TaskDTO removeLabelFromTask(Long taskId, Long labelId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));

        task.getLabels().remove(label);
        Task saved = taskRepository.save(task);
        return TaskMapper.mapToTaskDTO(saved);
    }

    // this is required for scheduler
    @Override
    public List<Task> getAllTasksEntities() {
        return taskRepository.findAll();
    }

}
