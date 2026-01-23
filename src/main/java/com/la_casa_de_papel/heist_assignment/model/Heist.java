package com.la_casa_de_papel.heist_assignment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Heist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String location;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @OneToMany(mappedBy = "heist", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HeistSkill> heistSkills = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private HeistStatus status = HeistStatus.PLANNING;

}