package com.example.task_manager.repository;

import com.example.task_manager.model.Task;
import com.example.task_manager.model.enums.TaskPriority;
import com.example.task_manager.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(TaskPriority priority);

    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);

    List<Task> findByTitleContainingIgnoreCase(String title);

    List<Task> findByDescriptionContainingIgnoreCase(String description);

    List<Task> findByUserId(Long userId);

    List<Task> findByDueDate(LocalDate dueDate);

    // Optional: search across multiple fields
    @Query("SELECT t FROM Task t WHERE " +
            "(:title IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
            "(:description IS NULL OR LOWER(t.description) LIKE LOWER(CONCAT('%', :description, '%'))) AND " +
            "(:userId IS NULL OR t.user.id = :userId) AND " +
            "(:dueDate IS NULL OR t.dueDate = :dueDate)")
    List<Task> searchTasks(@Param("title") String title,
                           @Param("description") String description,
                           @Param("userId") Long userId,
                           @Param("dueDate") LocalDate dueDate);
}
