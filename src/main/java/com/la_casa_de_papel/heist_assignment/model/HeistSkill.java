package com.la_casa_de_papel.heist_assignment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeistSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String level;

    private Integer members;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "heist_id")
    private Heist heist;
}
