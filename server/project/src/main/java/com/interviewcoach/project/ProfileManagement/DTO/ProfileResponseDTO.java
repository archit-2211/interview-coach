package com.interviewcoach.project.ProfileManagement.DTO;

import java.util.List;

import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.enums.UserStatus;


import jakarta.validation.constraints.Email;

public record ProfileResponseDTO(
                @Email
                String email,
                String name,

                String phoneNumber,
                UserRole role,
                UserStatus status,
                double rating,
                List<String> skills

) {
}
