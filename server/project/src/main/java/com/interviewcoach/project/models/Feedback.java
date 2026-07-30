package com.interviewcoach.project.models;

import java.util.UUID;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
public class Feedback {
    @Id
    private UUID feedbackId ; 
    @Embedded
    private InterviewersFeedback interviewersFeedback ; 
    @Embedded
    private CandidatesAssessment candidatesAssessment ; 
}
