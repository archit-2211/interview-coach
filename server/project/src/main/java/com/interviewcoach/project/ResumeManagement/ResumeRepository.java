package com.interviewcoach.project.ResumeManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Resume;


public interface ResumeRepository
        extends JpaRepository<Resume, UUID> {

    List<Resume> findByProfile(
            Profile profile
    );
}