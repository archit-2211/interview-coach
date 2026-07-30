package com.interviewcoach.project.InterviewManagement.dtos;

import java.util.List;
import java.util.UUID;

public record IRDetailsDTO(
    UUID slotId, 
    String interviewerEmail , 
    List<String> topics, 
    String description
) {
    
}
