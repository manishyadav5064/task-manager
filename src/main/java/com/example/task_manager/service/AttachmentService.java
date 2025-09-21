package com.example.task_manager.service;

import com.example.task_manager.dto.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {
    AttachmentDTO uploadAttachment(Long taskId, MultipartFile file) throws IOException;

    List<AttachmentDTO> getAttachmentsByTask(Long taskId);

    byte[] downloadAttachment(Long attachmentId);
}
