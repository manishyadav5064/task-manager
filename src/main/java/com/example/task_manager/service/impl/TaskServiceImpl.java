package com.example.task_manager.service.impl;

import com.example.task_manager.dto.DashboardDTO;
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
import com.example.task_manager.security.utils.SecurityUtils;
import com.example.task_manager.service.TaskHistoryService;
import com.example.task_manager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskHistoryService taskHistoryService;
    private final SecurityUtils securityUtils;

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        User currentUser = securityUtils.getCurrentUserEntity();
        Long userIdInRequest = taskDTO.getUserId();

        if (!securityUtils.isAdmin(currentUser)) {
            // Normal user can only create tasks for themselves
            if (userIdInRequest == null || !userIdInRequest.equals(currentUser.getId())) {
                throw new SecurityException("You can only create tasks for yourself, Please pass your own userId in request body");
            }
        }

        Task task = TaskMapper.mapToTask(taskDTO);

        if (userIdInRequest != null) {
            User user = userRepository.findById(userIdInRequest)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            task.setUser(user);
        }

        Task savedTask = taskRepository.save(task);

        // Record creation in history
        taskHistoryService.recordChange(
                savedTask.getId(),
                userIdInRequest,
                "TASK_CREATED",
                null,
                "Task created with title: " + savedTask.getTitle()
        );

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
        User currentUser = securityUtils.getCurrentUserEntity();

        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!securityUtils.isOwner(existingTask.getUser(), currentUser) && !securityUtils.isAdmin(currentUser)) {
            throw new SecurityException("You can only update your own tasks");
        }

        // Compare old values
        if (!existingTask.getTitle().equals(taskDTO.getTitle())) {
            taskHistoryService.recordChange(id, existingTask.getUser() != null ? existingTask.getUser().getId() : null,
                    "title", existingTask.getTitle(), taskDTO.getTitle());
        }

        if (!existingTask.getDescription().equals(taskDTO.getDescription())) {
            taskHistoryService.recordChange(id, existingTask.getUser() != null ? existingTask.getUser().getId() : null,
                    "description", existingTask.getDescription(), taskDTO.getDescription());
        }

        if (existingTask.getDueDate() != null && !existingTask.getDueDate().equals(taskDTO.getDueDate())
                || existingTask.getDueDate() == null && taskDTO.getDueDate() != null) {
            taskHistoryService.recordChange(id, existingTask.getUser() != null ? existingTask.getUser().getId() : null,
                    "dueDate",
                    existingTask.getDueDate() != null ? existingTask.getDueDate().toString() : null,
                    taskDTO.getDueDate() != null ? taskDTO.getDueDate().toString() : null);
        }

        if (existingTask.getIsCompleted() != null && !existingTask.getIsCompleted().equals(taskDTO.getIsCompleted())
                || existingTask.getIsCompleted() == null && taskDTO.getIsCompleted() != null) {
            taskHistoryService.recordChange(id, existingTask.getUser() != null ? existingTask.getUser().getId() : null,
                    "isCompleted",
                    String.valueOf(existingTask.getIsCompleted()),
                    String.valueOf(taskDTO.getIsCompleted()));
        }

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
        User currentUser = securityUtils.getCurrentUserEntity();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Long oldUserId = task.getUser() != null ? task.getUser().getId() : null;
        String oldUserName = task.getUser() != null ? task.getUser().getUsername() : null;

        if (!securityUtils.isOwner(task.getUser(), currentUser) && !securityUtils.isAdmin(currentUser)) {
            throw new SecurityException("You can only reassign your own tasks");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));


        task.setUser(user);
        Task savedTask = taskRepository.save(task);

        // Record assignment change
        taskHistoryService.recordChange(
                taskId,
                userId,
                "assignment",
                oldUserName,
                task.getUser() != null ? task.getUser().getUsername() : null
        );

        return TaskMapper.mapToTaskDTO(savedTask);
    }

    @Override
    public void deleteTask(Long id) {
        User currentUser = securityUtils.getCurrentUserEntity();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (!securityUtils.isOwner(task.getUser(), currentUser) && !securityUtils.isAdmin(currentUser)) {
            throw new SecurityException("You can only delete your own tasks");
        }

        taskRepository.delete(task);
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

        taskHistoryService.recordChange(taskId,
                task.getUser() != null ? task.getUser().getId() : null,
                "label_added",
                null,
                label.getName());

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

        taskHistoryService.recordChange(taskId,
                task.getUser() != null ? task.getUser().getId() : null,
                "label_removed",
                label.getName(),
                null);

        return TaskMapper.mapToTaskDTO(saved);
    }

    // this is required for scheduler
    @Override
    public List<Task> getAllTasksEntities() {
        return taskRepository.findAll();
    }

    @Override
    public DashboardDTO getDashboardStatistics(Long userId) {
        List<Task> tasks;
        if (userId != null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            tasks = user.getTasks();
        } else {
            tasks = taskRepository.findAll();
        }

        Map<TaskStatus, Long> byStatus = tasks.stream()
                .collect(Collectors.groupingBy(Task::getStatus, Collectors.counting()));

        Map<TaskPriority, Long> byPriority = tasks.stream()
                .collect(Collectors.groupingBy(Task::getPriority, Collectors.counting()));

        Map<String, Long> byLabel = tasks.stream()
                .flatMap(t -> t.getLabels().stream())
                .collect(Collectors.groupingBy(Label::getName, Collectors.counting()));

        Map<String, Long> byUser = tasks.stream()
                .filter(t -> t.getUser() != null)
                .collect(Collectors.groupingBy(t -> t.getUser().getUsername(), Collectors.counting()));

        LocalDate today = LocalDate.now();
        long dueToday = tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isEqual(today))
                .count();

        long overdue = tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isBefore(today) && !t.getIsCompleted())
                .count();

        List<TaskDTO> upcomingTasks = tasks.stream()
                .filter(t -> t.getDueDate() != null && t.getDueDate().isAfter(today))
                .sorted((t1, t2) -> t1.getDueDate().compareTo(t2.getDueDate()))
                .limit(5)
                .map(TaskMapper::mapToTaskDTO)
                .toList();

        return new DashboardDTO(
                (long) tasks.size(),
                byStatus,
                byPriority,
                byLabel,
                byUser,
                dueToday,
                overdue,
                upcomingTasks
        );
    }
}
