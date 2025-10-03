package com.example.task_manager.security;

import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.model.Comment;
import com.example.task_manager.model.Task;
import com.example.task_manager.model.User;
import com.example.task_manager.model.enums.Role;
import com.example.task_manager.repository.CommentRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.security.annotation.CheckOwnership;
import com.example.task_manager.security.utils.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

@Aspect
@Component
public class OwnershipAspect {

    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    public OwnershipAspect(TaskRepository taskRepository, CommentRepository commentRepository, UserRepository userRepository, SecurityUtils securityUtils) {
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.securityUtils = securityUtils;
    }

    @Around("@annotation(com.example.task_manager.security.CheckOwnership)")
    public Object checkOwnership(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckOwnership annotation = method.getAnnotation(CheckOwnership.class);

        Object[] args = joinPoint.getArgs();
        String idParam = annotation.idParam();
        Long entityId = null;

        // Find the entityId argument by parameter name
        String[] paramNames = signature.getParameterNames();
        for (int i = 0; i < paramNames.length; i++) {
            if (paramNames[i].equals(idParam)) {
                entityId = (Long) args[i];
                break;
            }
        }

        if (entityId == null) {
            throw new IllegalArgumentException("Entity ID parameter not found: " + idParam);
        }

        User currentUser = getCurrentUser();

        switch (annotation.entity()) {
            case TASK -> {
                Task task = taskRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

                if (!isAdmin(currentUser) && !isOwner(task.getUser(), currentUser)) {
                    throw new SecurityException("You can only access your own tasks");
                }
            }
            case COMMENT -> {
                Comment comment = commentRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

                if (!isAdmin(currentUser) && !isOwner(comment.getUser(), currentUser)) {
                    throw new SecurityException("You can only access your own comments");
                }
            }
        }

        return joinPoint.proceed();
    }

    private boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    private boolean isOwner(User owner, User currentUser) {
        return owner != null && owner.getId().equals(currentUser.getId());
    }

    private User getCurrentUser() {
        String username = securityUtils.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
    }
}
