package com.example.task_manager.controller;

import com.example.task_manager.dto.LabelDTO;
import com.example.task_manager.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    // Create a label
    @PostMapping
    public ResponseEntity<LabelDTO> createLabel(@RequestBody LabelDTO labelDTO) {
        LabelDTO createdLabel = labelService.createLabel(labelDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLabel);
    }

    // Get all labels
    @GetMapping
    public ResponseEntity<List<LabelDTO>> getAllLabels() {
        List<LabelDTO> labels = labelService.getAllLabels();
        return ResponseEntity.ok(labels);
    }

    // Get label by ID
    @GetMapping("/{id}")
    public ResponseEntity<LabelDTO> getLabelById(@PathVariable Long id) {
        LabelDTO label = labelService.getLabelById(id);
        return ResponseEntity.ok(label);
    }
}
