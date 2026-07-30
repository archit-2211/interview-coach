package com.interviewcoach.project.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import com.interviewcoach.project.enums.InterviewRequestStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;


import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter

public class InterviewRequest {

    @Id
    private UUID requestId ; 
    @ManyToOne
    @JoinColumn(name = "candidate_profile_id")
    private Profile candidateProfile ;
    @ManyToOne
    @JoinColumn(name = "interviewer_profile_id")
    private Profile interviewerProfile ; 
    @ManyToOne
    @JoinColumn(name = "slot_id")
    private Slot slot ; 
    @ManyToMany
    @JoinTable(name = "interview_request_topics", joinColumns = @JoinColumn(name = "request_id"),inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> topics = new ArrayList<>() ; 
    @Enumerated(EnumType.STRING)
    private InterviewRequestStatus interviewRequestStatus = InterviewRequestStatus.PENDING ; 
    private LocalDateTime createdAt = LocalDateTime.now();
    private String candidateNotes ; 
    
    
}



