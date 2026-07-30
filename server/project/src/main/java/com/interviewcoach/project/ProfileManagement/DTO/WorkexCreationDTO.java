package com.interviewcoach.project.ProfileManagement.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record WorkexCreationDTO(
    @NotNull
    String companyName, 
    @NotNull
    LocalDate startDate , 
    LocalDate endDate 
) {
    
}
