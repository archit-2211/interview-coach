package com.interviewcoach.project.InterviewManagement.dtos;

public record FeedbackResponseDTO (
    String candidateAssessment, 
    String interviewerFeedback,
    int candidateRating, 
    int interviewerRating) {

}