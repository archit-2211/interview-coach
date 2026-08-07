package com.interviewcoach.project.auth.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;



public record RegisterDTO(

    @NotBlank(message = "Name cannot be blank")
    String name,

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(
        regexp = "^[6-9]\\d{9}$",
        message = "Enter a valid 10-digit Indian phone number"
    )
    String phoneNumber,

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Enter a valid email")
    String email,

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,

    @NotNull(message = "Role is required")
    RoleDTO role

) {}