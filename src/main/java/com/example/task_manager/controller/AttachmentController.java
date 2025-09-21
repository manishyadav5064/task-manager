package com.example.task_manager.controller;

import com.example.task_manager.dto.AttachmentDTO;
import com.example.task_manager.service.AttachmentService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @PostMapping("/upload/{taskId}")
    public AttachmentDTO uploadAttachment(@PathVariable Long taskId, @RequestParam("file") MultipartFile file) throws IOException {
        return attachmentService.uploadAttachment(taskId, file);
    }

    @GetMapping("/task/{taskId}")
    public List<AttachmentDTO> getAttachmentsByTask(@PathVariable Long taskId) {
        return attachmentService.getAttachmentsByTask(taskId);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        byte[] data = attachmentService.downloadAttachment(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}
