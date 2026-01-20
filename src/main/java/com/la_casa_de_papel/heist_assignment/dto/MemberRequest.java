package com.la_casa_de_papel.heist_assignment.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class MemberRequest {
    @NotBlank(message = "Name is required.")
    private String name;

    @Pattern(regexp = "F|M", message = "Sex must be F or M.")
    private String sex;

    @NotBlank
    @Email(message = "Email must be valid.")
    private String email;

    @Valid
    private List<SkillRequest> skills;

    private String mainSkill;

    @Pattern(regexp = "AVAILABLE|EXPIRED|INCARCERATED|RETIRED", message = "Invalid status")
    private String status;
}
