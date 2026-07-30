package com.interviewcoach.project.SlotManagement.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record SlotDTO(UUID slotId , LocalDate date, LocalTime startTime , LocalTime endTime ){
    
}
