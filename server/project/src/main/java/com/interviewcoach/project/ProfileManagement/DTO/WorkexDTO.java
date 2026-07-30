package com.interviewcoach.project.ProfileManagement.DTO;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record WorkexDTO(
    UUID id ,
    @NotNull
    String companyName, 
    @NotNull
    LocalDate startDate , 
    LocalDate endDate 
) {
    
}
