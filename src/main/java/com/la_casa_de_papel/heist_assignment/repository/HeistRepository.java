package com.la_casa_de_papel.heist_assignment.repository;

import com.la_casa_de_papel.heist_assignment.model.Heist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeistRepository extends JpaRepository<Heist, Long> {
    boolean existsByName(String name);
}
