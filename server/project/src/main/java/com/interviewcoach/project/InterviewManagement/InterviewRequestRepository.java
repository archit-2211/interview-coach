package com.interviewcoach.project.InterviewManagement;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.enums.InterviewRequestStatus;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Slot;

public interface InterviewRequestRepository extends JpaRepository<InterviewRequest, UUID> {
    boolean existsByCandidateProfileAndSlot(Profile candidateProfile, Slot slot) ; 
    
    List<InterviewRequest>findByInterviewerProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(String email,InterviewRequestStatus status);
    List<InterviewRequest>findByInterviewerProfileUserEmailOrderByCreatedAtDesc(String email);
        List<InterviewRequest>findByCandidateProfileUserEmailAndInterviewRequestStatusOrderByCreatedAtDesc(String email,InterviewRequestStatus status);
    List<InterviewRequest>findByCandidateProfileUserEmailOrderByCreatedAtDesc(String email);
}
