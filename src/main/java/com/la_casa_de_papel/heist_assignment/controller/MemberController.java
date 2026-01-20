package com.la_casa_de_papel.heist_assignment.controller;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;
import com.la_casa_de_papel.heist_assignment.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity<Void> createMember(@Valid @RequestBody MemberRequest request){

        Long member_id = memberService.createMember(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(member_id)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
