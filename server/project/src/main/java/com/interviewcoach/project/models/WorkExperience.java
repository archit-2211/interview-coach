package com.interviewcoach.project.models;

import java.time.LocalDate;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkExperience {
    @Id
    private UUID workExperienceId ; 
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile ; 
    @NotBlank
    @Size(max = 50)
    private String companyName ; 
    @NotNull
    private LocalDate startDate ; 
    private LocalDate endDate ; 

    

}


/*

## WorkExperience

Represents a professional work experience record.

### Attributes

| Field            | Type      | Description           |
| ---------------- | --------- | --------------------- |
| workExperienceId | UUID      | Unique identifier     |
| profile          | Profile   | Associated profile    |
| companyName      | String    | Company name          |
| startDate        | LocalDate | Employment start date |
| endDate          | LocalDate | Employment end date   |

---


*/