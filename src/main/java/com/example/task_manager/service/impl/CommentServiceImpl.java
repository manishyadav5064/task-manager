package com.example.task_manager.service.impl;

import com.example.task_manager.dto.CommentDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.CommentMapper;
import com.example.task_manager.model.Comment;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.repository.CommentRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDTO addComment(CommentDTO commentDTO) {
        Comment comment = CommentMapper.toEntity(commentDTO);

        Task task = taskRepository.findById(commentDTO.getTaskId())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));
        comment.setTask(task);

        if (commentDTO.getUserId() != null) {
            User user = userRepository.findById(commentDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            comment.setUser(user);
        }

        Comment saved = commentRepository.save(comment);
        return CommentMapper.toDTO(saved);
    }

    @Override
    public List<CommentDTO> getCommentsByTask(Long taskId) {
        return commentRepository.findByTaskId(taskId).stream()
                .map(CommentMapper::toDTO)
                .collect(Collectors.toList());
    }
}
