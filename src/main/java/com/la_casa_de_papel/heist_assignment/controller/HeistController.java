package com.la_casa_de_papel.heist_assignment.controller;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateHeistSkillsRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateSkillsRequest;
import com.la_casa_de_papel.heist_assignment.service.HeistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class HeistController {

    private final HeistService heistService;

    @PostMapping("/heist")
    public ResponseEntity<Void> createHeist(@Valid @RequestBody HeistRequest request){
        Long id = heistService.createHeist(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/heist/{id}/skills")
    public ResponseEntity<Void> updateHeistSkill(
            @PathVariable Long id,
            @Valid @RequestBody UpdateHeistSkillsRequest request) {
        heistService.updateHeistSkills(id, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity.noContent()
                .header("Content-Location", location.toString())
                .build();
    }
}
