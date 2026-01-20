package com.la_casa_de_papel.heist_assignment.repository;

import com.la_casa_de_papel.heist_assignment.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository  extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
}
