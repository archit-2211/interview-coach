package com.interviewcoach.project.InterviewManagement.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import com.interviewcoach.project.enums.InterviewRequestStatus;

public record InterviewRequestDTO(
    UUID requestId,
    UUID slotId, 
    String description, 
    List<String> topics ,
    InterviewRequestStatus status,
    String candidateEmail,
    String interviewerEmail,
    LocalDate slotDate, 
    LocalTime slotStartTime, 
    LocalTime slotEndTime 



) {
    
}
