package com.interviewcoach.project.ProfileManagement;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.User;

import java.util.List;
import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Profile, UUID> {

    Optional<Profile>  findByUser(User user);
    Optional<Profile> findByUserEmail(String email);
    @Query("""
        SELECT DISTINCT p
        FROM Profile p
        JOIN p.skills s
        WHERE s.skillName IN :skills
        AND p.user.userRole = 'INTERVIEWER'
        ORDER BY p.rating DESC
        """)
        Page<Profile> findInterviewersBySkills(
                List<String> skills,
                Pageable pageable
        );
    
}
