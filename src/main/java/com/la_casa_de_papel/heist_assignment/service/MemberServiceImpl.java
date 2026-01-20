package com.la_casa_de_papel.heist_assignment.service;

import com.la_casa_de_papel.heist_assignment.dto.MemberRequest;
import com.la_casa_de_papel.heist_assignment.dto.SkillRequest;
import com.la_casa_de_papel.heist_assignment.model.Member;
import com.la_casa_de_papel.heist_assignment.model.Sex;
import com.la_casa_de_papel.heist_assignment.model.Skill;
import com.la_casa_de_papel.heist_assignment.model.Status;
import com.la_casa_de_papel.heist_assignment.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
}
