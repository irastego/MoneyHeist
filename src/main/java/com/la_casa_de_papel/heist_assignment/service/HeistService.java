package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;

public interface HeistService {
    Long createHeist(HeistRequest request);
}
