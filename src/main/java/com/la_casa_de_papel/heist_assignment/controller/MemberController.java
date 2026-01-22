package com.la_casa_de_papel.heist_assignment.controller;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateSkillsRequest;
import com.la_casa_de_papel.heist_assignment.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PutMapping("/member/{id}/skills")
    public ResponseEntity<Void> updateSkills(
            @PathVariable Long id,
            @Valid @RequestBody UpdateSkillsRequest request) {

        memberService.updateSkills(id, request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .build()
                .toUri();

        return ResponseEntity
                .noContent()
                .header("Content-Location", location.toString())
                .build();
    }

    @DeleteMapping("/member/{id}/skills/{skillName}")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable Long id,
            @PathVariable String skillName
    ){
        memberService.deleteSkill(id, skillName);

        return ResponseEntity.noContent().build();
    }
}
