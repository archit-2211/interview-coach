package com.interviewcoach.project.models;

import java.util.UUID;

import com.interviewcoach.project.enums.InterviewStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Interview {

    @Id
    private UUID interviewId ; 
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interview_request_id")
    private InterviewRequest interviewRequest ; 
    @Enumerated(EnumType.STRING)
    private InterviewStatus interviewStatus ; 
    private String meetingLink ; 
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true,  fetch = FetchType.EAGER)
    @JoinColumn(name = "feedback_id")
    private Feedback feedback ;
    private boolean feedbackSubmitted = false ; 
    private boolean assessmentSubmitted = false ; 
    
    
}
