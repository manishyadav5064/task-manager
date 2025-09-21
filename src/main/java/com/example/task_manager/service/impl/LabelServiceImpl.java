package com.example.task_manager.service.impl;

import com.example.task_manager.dto.LabelDTO;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.mapper.LabelMapper;
import com.example.task_manager.model.Label;
import com.example.task_manager.repository.LabelRepository;
import com.example.task_manager.service.LabelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public LabelDTO createLabel(LabelDTO labelDTO) {
        // Check if label already exists
        if (labelRepository.findByName(labelDTO.getName()).isPresent()) {
            throw new RuntimeException("Label already exists");
        }
        Label label = LabelMapper.toEntity(labelDTO);
        Label saved = labelRepository.save(label);
        return LabelMapper.toDTO(saved);
    }

    @Override
    public List<LabelDTO> getAllLabels() {
        return labelRepository.findAll().stream()
                .map(LabelMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LabelDTO getLabelById(Long id) {
        Label label = labelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Label not found"));
        return LabelMapper.toDTO(label);
    }
}
