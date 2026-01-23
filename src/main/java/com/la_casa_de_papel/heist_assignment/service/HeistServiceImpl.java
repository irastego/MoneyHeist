package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;
import com.la_casa_de_papel.heist_assignment.dto.HeistSkillRequest;
import com.la_casa_de_papel.heist_assignment.model.Heist;
import com.la_casa_de_papel.heist_assignment.model.HeistSkill;
import com.la_casa_de_papel.heist_assignment.model.HeistStatus;
import com.la_casa_de_papel.heist_assignment.repository.HeistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HeistServiceImpl implements HeistService{

    private final HeistRepository heistRepository;

    @Override
    public Long createHeist(HeistRequest request){
        if(heistRepository.existsByName(request.getName())){
            throw new RuntimeException("Heist name already exists.");
        }

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new RuntimeException("Start time must be before end time");
        }

        if(request.getEndTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("End time cannot be in the past.");
        }

        Set<String> uniqueSkillCheck = new HashSet<>();

        Heist heist = new Heist();
        heist.setName(request.getName());
        heist.setLocation(request.getLocation());
        heist.setStartTime(request.getStartTime());
        heist.setEndTime(request.getEndTime());
        heist.setStatus(HeistStatus.PLANNING);

        List<HeistSkill> skillEntities = new ArrayList<>();

        for(HeistSkillRequest skillDto : request.getSkills()){
            String uniqueKey = skillDto.getName().toLowerCase() + "-" + skillDto.getLevel();

            if(!uniqueSkillCheck.add(uniqueKey)){
                throw new RuntimeException("Duplicate skill (name + level) found: " + skillDto.getName());
            }

            HeistSkill skillEntity = new HeistSkill();
            skillEntity.setName(skillDto.getName().toLowerCase());
            skillEntity.setLevel(skillDto.getLevel());
            skillEntity.setMembers(skillDto.getMembers());

            skillEntity.setHeist(heist);

            skillEntities.add(skillEntity);
        }

        heist.setHeistSkills(skillEntities);

        heistRepository.save(heist);

        return heist.getId();
    }
}
