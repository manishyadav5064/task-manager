package com.example.task_manager.mapper;

import com.example.task_manager.dto.AttachmentDTO;
import com.example.task_manager.model.Attachment;

public class AttachmentMapper {

    public static AttachmentDTO toDTO(Attachment attachment) {
        AttachmentDTO dto = new AttachmentDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileType(attachment.getFileType());
        dto.setTaskId(attachment.getTask() != null ? attachment.getTask().getId() : null);
        dto.setUploadedAt(attachment.getUploadedAt());
        return dto;
    }
}
