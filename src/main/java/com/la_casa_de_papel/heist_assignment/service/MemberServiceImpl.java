package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;
import com.la_casa_de_papel.heist_assignment.dto.SkillRequest;
import com.la_casa_de_papel.heist_assignment.dto.UpdateSkillsRequest;
import com.la_casa_de_papel.heist_assignment.exception.MemberNotFoundException;
import com.la_casa_de_papel.heist_assignment.model.Member;
import com.la_casa_de_papel.heist_assignment.model.Sex;
import com.la_casa_de_papel.heist_assignment.model.Skill;
import com.la_casa_de_papel.heist_assignment.model.Status;
import com.la_casa_de_papel.heist_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Long createMember(MemberRequest request){
        if(memberRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists.");
        }

        Member member = new Member();
        member.setName(request.getName());
        member.setSex(Sex.valueOf(request.getSex()));
        member.setEmail(request.getEmail());
        member.setStatus(Status.valueOf(request.getStatus()));

        Set<String> uniqueSkillNames = new HashSet<>();

        for(SkillRequest skillDto : request.getSkills()){
            String lowerCaseName = skillDto.getName().toLowerCase();
            if(!uniqueSkillNames.add(lowerCaseName)){
                throw new RuntimeException("Duplicate skill found: " + skillDto.getName());
            }

            Skill skill = new Skill();
            skill.setName(skillDto.getName().toLowerCase());

            String level = skillDto.getLevel() == null ? "*" : skillDto.getLevel();
            skill.setLevel(level);

            skill.setMember(member);
            member.getSkills().add(skill);
        }


        if(request.getMainSkill() != null){
            boolean mainSkillExists = member.getSkills().stream()
                    .anyMatch(s -> s.getName().equalsIgnoreCase(request.getMainSkill()));

            if(!mainSkillExists){
                throw new RuntimeException("Main skill must be one of the skills provided.");
            }

            member.setMainSkill(request.getMainSkill().toLowerCase());
        }

        Member savedMember = memberRepository.save(member);
        return savedMember.getId();
    }

    @Override
    public void updateSkills(Long memberId, UpdateSkillsRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("Member not found with id: " + memberId));

        if(request.getSkills().isEmpty() && request.getMainSkill() == null){
            throw new RuntimeException("Either skills or mainSkill must be provided.");
        }

        Set<String> requestSkillNames = new HashSet<>();

        for(SkillRequest skillDto : request.getSkills()){
            String lowerName = skillDto.getName().toLowerCase();

            if(!requestSkillNames.add(lowerName)){
                throw new RuntimeException("Duplicate skill found in request: " + skillDto.getName());
            }

            Optional<Skill> existingSkillOpt = member.getSkills().stream()
                    .filter(s -> s.getName().equalsIgnoreCase(lowerName))
                    .findFirst();

            if(existingSkillOpt.isPresent()){
                Skill existingSkill = existingSkillOpt.get();

                if(skillDto.getLevel() != null){
                    existingSkill.setLevel((skillDto.getLevel()));
                }
            } else{
                Skill newSkill = new Skill();
                newSkill.setName(lowerName);
                newSkill.setLevel(skillDto.getLevel() == null ? "*" : skillDto.getLevel());

                newSkill.setMember(member);
                member.getSkills().add(newSkill);
            }

            if(request.getMainSkill() != null){
                String newMainSkill = request.getMainSkill().toLowerCase();

                boolean skillExists = member.getSkills().stream()
                        .anyMatch(s -> s.getName().equalsIgnoreCase(newMainSkill));

                if(!skillExists){
                    throw new RuntimeException("Main skill must be one of the member's skills");
                }

                member.setMainSkill(newMainSkill);
            }
            memberRepository.save(member);
        }

    }
}
