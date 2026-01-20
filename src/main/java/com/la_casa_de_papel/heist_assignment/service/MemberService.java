package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;

public interface MemberService {
    Long createMember(MemberRequest request);
}
