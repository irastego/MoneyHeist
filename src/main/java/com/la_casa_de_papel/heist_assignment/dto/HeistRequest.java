package com.la_casa_de_papel.heist_assignment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class HeistRequest {

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Start time is required.")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required.")
    private LocalDateTime endTime;

    @Valid
    private List<HeistSkillRequest> skills;

}