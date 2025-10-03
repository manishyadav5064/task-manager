package com.example.task_manager.service;

import com.example.task_manager.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO addComment(CommentDTO commentDTO);

    List<CommentDTO> getCommentsByTask(Long taskId);

    void deleteComment(Long commentId);
}
