package com.example.task_manager.service.impl;

import com.example.task_manager.dto.AttachmentDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.AttachmentMapper;
import com.example.task_manager.model.Attachment;
import com.example.task_manager.model.Task;
import com.example.task_manager.repository.AttachmentRepository;
import com.example.task_manager.repository.TaskRepository;
import com.example.task_manager.service.AttachmentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;

    public AttachmentServiceImpl(AttachmentRepository attachmentRepository, TaskRepository taskRepository) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public AttachmentDTO uploadAttachment(Long taskId, MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(file.getContentType());
        attachment.setData(file.getBytes());
        attachment.setTask(task);

        Attachment saved = attachmentRepository.save(attachment);
        return AttachmentMapper.toDTO(saved);
    }

    @Override
    public List<AttachmentDTO> getAttachmentsByTask(Long taskId) {
        return attachmentRepository.findByTaskId(taskId).stream()
                .map(AttachmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadAttachment(Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment not found"));
        return attachment.getData();
    }
}
