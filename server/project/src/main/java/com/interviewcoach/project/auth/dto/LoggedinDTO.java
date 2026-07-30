package com.interviewcoach.project.auth.dto;

import com.interviewcoach.project.enums.UserRole;

public record LoggedinDTO(String bearerToken, String refreshToken) {
    
}
