package com.la_casa_de_papel.heist_assignment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HeistSkillRequest {
    @NotBlank(message = "Skill name is required.")
    private String name;

    @NotBlank(message = "Level is required.")
    private String level;

    @NotNull(message = "Number of members is required.")
    private Integer members;

}