package com.interviewcoach.project.ResumeManagement.dto;

import java.time.LocalDateTime;
import java.util.UUID;


public record ResumeResponseDTO(UUID id, String name, String url,LocalDateTime uploadedAt) {
    
}
