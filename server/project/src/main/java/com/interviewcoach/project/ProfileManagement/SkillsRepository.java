package com.interviewcoach.project.ProfileManagement;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.models.Skill;
import java.util.List;


public interface SkillsRepository extends JpaRepository<Skill, UUID> {
    List<Skill> findBySkillNameIn(List<String> skillNames);
    
}
