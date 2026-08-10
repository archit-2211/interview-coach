package com.interviewcoach.project.models;

import java.time.LocalDateTime;
import java.util.UUID;

import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.enums.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Builder
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private UUID userId ; 
    @NotBlank
    @Email
    @Column(unique = true, nullable = false)
    private String email ; 
    private String encodedPassword ;
    private String name ;  
    @Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$")
    @Column(unique = true)
    private String phoneNumber ; 
    @Enumerated(EnumType.STRING)
    private UserRole userRole ; 
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus ; 
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now() ; 
    @Enumerated(EnumType.STRING)
    private AuthenticationSource authenticationSource ; 

    
}

/*

## User

Represents an authenticated user of the platform.

### Attributes

| Field                 | Type                  | Description                      |
| -----------           | ----------            | -------------------------------- |
| userId                | UUID                  | Unique identifier                |
| email                 | String                | User email address (unique)      |
| password              | String                | Encrypted password               |
| phoneNumber           | String                | Contact number                   |
| userRole              | UserRole              | Candidate, Interviewer, or Admin |
| createdAt             | DateTime              | TimeStamp of creation time.      |
| userStatus            | UserStatus            | Verification status              |
| name                  | String                | Name of the user                 |
| authenticationSource  | AuthenticationSource  | To provide AuthenticatonSource   |             |

---


*/