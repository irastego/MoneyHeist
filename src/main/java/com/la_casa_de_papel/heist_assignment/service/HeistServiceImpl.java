package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.HeistRequest;
import com.la_casa_de_papel.heist_assignment.dto.HeistSkillRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateHeistSkillsRequest;
import com.la_casa_de_papel.heist_assignment.exception.HeistNotEligibleException;
import com.la_casa_de_papel.heist_assignment.exception.HeistNotFoundException;
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

    @Override
    public void updateHeistSkills(Long heistId, UpdateHeistSkillsRequest request) {
        Heist heist = heistRepository.findById(heistId)
                .orElseThrow(() -> new HeistNotFoundException("Heist not found."));

        if(heist.getStatus() == HeistStatus.IN_PROGRESS || heist.getStatus() == HeistStatus.FINISHED){
            throw new HeistNotEligibleException("Heist has already started.");
        }

        Set<String> requestDuplicates = new HashSet<>();

        for(HeistSkillRequest skillDto : request.getSkills()) {

            String uniqueKey = skillDto.getName().toLowerCase() + "-" + skillDto.getLevel();

            if (!requestDuplicates.add(uniqueKey)) {
                throw new RuntimeException("Duplicate skill in request.");
            }

            java.util.Optional<HeistSkill> existingSkillOpt = heist.getHeistSkills().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(skillDto.getName())
                            && s.getLevel().equals(skillDto.getLevel()))
                    .findFirst();

            if (existingSkillOpt.isPresent()) {
                existingSkillOpt.get().setMembers(skillDto.getMembers());
            } else {
                HeistSkill newSkill = new HeistSkill();
                newSkill.setName(skillDto.getName().toLowerCase());
                newSkill.setLevel(skillDto.getLevel());
                newSkill.setMembers(skillDto.getMembers());

                newSkill.setHeist(heist);

                heist.getHeistSkills().add(newSkill);
            }
        }
        heistRepository.save(heist);
    }
}
