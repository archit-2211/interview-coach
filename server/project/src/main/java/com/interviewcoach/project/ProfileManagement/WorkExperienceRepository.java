package com.interviewcoach.project.ProfileManagement;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.interviewcoach.project.models.WorkExperience;

import jakarta.transaction.Transactional;

public interface WorkExperienceRepository extends JpaRepository<WorkExperience, UUID> {

    @Modifying
    @Transactional
    @Query("DELETE FROM WorkExperience w WHERE w.workExperienceId = :id")
    void deleteWorkExperienceById(@Param("id") UUID id);
    
    
}
