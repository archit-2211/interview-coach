package com.interviewcoach.project.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginDTO (
    @NotNull(message = "Email Cannot be Empty")
    String email,
    @NotNull(message = "Password Cannot be empty")
    String password ){
    
}
