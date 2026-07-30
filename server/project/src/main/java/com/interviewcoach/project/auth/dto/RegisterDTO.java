package com.interviewcoach.project.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record RegisterDTO(
    String name,
    String phoneNumber , 
    @NotNull(message = "email cannot be null")
    @Email(message = "enter a valid email")
    String email, 
    @NotNull(message =  "password cannot be null")
    @Size(min = 8, message = "Password Must be minimum 8 characters long")
    String password,
    RoleDTO role 

) {
    
}
