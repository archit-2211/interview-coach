package com.interviewcoach.project.InterviewManagement.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SlotResponseDTO(
    UUID slotId ,
    LocalDate slotDate, 
    LocalTime slotStartTime , 
    LocalTime slotEndTime 


) {

    
}
