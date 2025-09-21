package com.example.task_manager.controller;

import com.example.task_manager.dto.LabelDTO;
import com.example.task_manager.service.LabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public LabelDTO createLabel(@RequestBody LabelDTO labelDTO) {
        return labelService.createLabel(labelDTO);
    }

    @GetMapping
    public List<LabelDTO> getAllLabels() {
        return labelService.getAllLabels();
    }

    @GetMapping("/{id}")
    public LabelDTO getLabelById(@PathVariable Long id) {
        return labelService.getLabelById(id);
    }
}
