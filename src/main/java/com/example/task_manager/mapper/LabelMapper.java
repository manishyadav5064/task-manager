package com.example.task_manager.mapper;

import com.example.task_manager.dto.LabelDTO;
import com.example.task_manager.model.Label;

public class LabelMapper {

    public static LabelDTO toDTO(Label label) {
        return new LabelDTO(label.getId(), label.getName());
    }

    public static Label toEntity(LabelDTO dto) {
        Label label = new Label();
        label.setId(dto.getId());
        label.setName(dto.getName());
        return label;
    }
}
