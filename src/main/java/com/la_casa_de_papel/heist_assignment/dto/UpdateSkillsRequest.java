package com.la_casa_de_papel.heist_assignment.dto;

import jakarta.validation.Valid;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class UpdateSkillsRequest {
    @Valid
    private List<SkillRequest> skills = new ArrayList<>();
    private String mainSkill;
}
