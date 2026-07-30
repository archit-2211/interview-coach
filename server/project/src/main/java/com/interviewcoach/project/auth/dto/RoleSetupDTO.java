package com.interviewcoach.project.auth.dto;

public record RoleSetupDTO(
    RoleDTO role, 
    String jwtToken 

) {

    
}
