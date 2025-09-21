package com.example.task_manager.service;

import com.example.task_manager.dto.LabelDTO;

import java.util.List;

public interface LabelService {
    LabelDTO createLabel(LabelDTO labelDTO);

    List<LabelDTO> getAllLabels();

    LabelDTO getLabelById(Long id);
}
