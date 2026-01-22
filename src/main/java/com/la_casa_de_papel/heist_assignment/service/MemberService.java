package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateSkillsRequest;

public interface MemberService {
    Long createMember(MemberRequest request);

    void updateSkills(Long memberId, UpdateSkillsRequest request);

    void deleteSkill(Long memberId, String skillName);
}
