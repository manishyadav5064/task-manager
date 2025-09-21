package com.example.task_manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task_history")
public class TaskHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // who made the change

    @Column(nullable = false)
    private String fieldChanged;

    @Column(nullable = false)
    private String oldValue;

    @Column(nullable = false)
    private String newValue;

    private LocalDateTime changedAt = LocalDateTime.now();
}
