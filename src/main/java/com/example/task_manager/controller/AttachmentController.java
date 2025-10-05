package com.example.task_manager.controller;

import com.example.task_manager.dto.AttachmentDTO;
import com.example.task_manager.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping("/upload/{taskId}")
    public ResponseEntity<AttachmentDTO> uploadAttachment(
            @PathVariable Long taskId,
            @RequestParam("file") MultipartFile file) throws IOException {
        AttachmentDTO attachmentDTO = attachmentService.uploadAttachment(taskId, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentDTO);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<AttachmentDTO>> getAttachmentsByTask(@PathVariable Long taskId) {
        List<AttachmentDTO> attachments = attachmentService.getAttachmentsByTask(taskId);
        return ResponseEntity.ok(attachments);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable Long id) {
        byte[] data = attachmentService.downloadAttachment(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "file_" + id);

        return ResponseEntity.ok()
                .headers(headers)
                .body(data);
    }
}
