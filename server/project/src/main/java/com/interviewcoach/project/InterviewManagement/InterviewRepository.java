package com.interviewcoach.project.InterviewManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.models.Interview;

public interface InterviewRepository extends JpaRepository<Interview, UUID> {
    List<Interview> findByInterviewRequestInterviewerProfileUserEmail(String email); 
    List<Interview> findByInterviewRequestCandidateProfileUserEmail(String email) ; 
    
}
