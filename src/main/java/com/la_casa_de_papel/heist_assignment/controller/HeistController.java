package com.la_casa_de_papel.heist_assignment.controller;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;
import com.la_casa_de_papel.heist_assignment.service.HeistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
}
