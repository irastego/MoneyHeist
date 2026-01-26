package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateHeistSkillsRequest;

public interface HeistService {
    Long createHeist(HeistRequest request);
    void updateHeistSkills(Long heistId, UpdateHeistSkillsRequest request);
}
