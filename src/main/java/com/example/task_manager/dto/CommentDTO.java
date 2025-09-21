package com.example.task_manager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    private Long id;

    @NotBlank(message = "Message cannot be empty")
    private String message;

    private Long taskId;

    private Long userId; // optional

    private LocalDateTime createdAt;
}
