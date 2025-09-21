package com.example.task_manager.mapper;

import com.example.task_manager.dto.CommentDTO;
import com.example.task_manager.model.Comment;

public class CommentMapper {

    public static CommentDTO toDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setMessage(comment.getMessage());
        dto.setTaskId(comment.getTask() != null ? comment.getTask().getId() : null);
        dto.setUserId(comment.getUser() != null ? comment.getUser().getId() : null);
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }

    public static Comment toEntity(CommentDTO dto) {
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setMessage(dto.getMessage());
        return comment;
    }
}
