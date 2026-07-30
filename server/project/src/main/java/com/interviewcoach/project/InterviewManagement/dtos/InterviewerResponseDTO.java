package com.interviewcoach.project.InterviewManagement.dtos;

import java.util.List;


public record InterviewerResponseDTO(
        String email ,
        String fullName,
        Double rating,
        List<String> skills
) {
}