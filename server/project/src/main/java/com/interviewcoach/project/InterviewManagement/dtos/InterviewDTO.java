package com.interviewcoach.project.InterviewManagement.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.interviewcoach.project.enums.InterviewStatus;

public record InterviewDTO(
    UUID interviewId, 
    String candidateEmail , 
    String interviewerEmail,
    InterviewStatus interviewStatus , 
    String meetingLink,
    LocalDate interviewDate, 
    LocalTime interviewStartTime, 
    LocalTime interviewEndTime ,
    FeedbackResponseDTO feedback ,
    boolean feedbackSubmitted,
    boolean assessmentSubmitted

) {
    
}
