package com.interviewcoach.project.InterviewManagement.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record FeedbackDTO(
    String description, 
    @Min(value = 1, message = "Rating should atleast be 1")
    @Max(value = 10, message = "Rating should at max 5")
    int rating



) {
    
}
