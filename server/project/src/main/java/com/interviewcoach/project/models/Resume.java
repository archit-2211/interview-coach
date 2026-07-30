package com.interviewcoach.project.models;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.validator.constraints.URL;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Resume {


    @Id
    private UUID resumeId ;
    @NotBlank
    private String fileName ; 
    @URL
    @NotBlank
    private String fileUrl ; 
    private LocalDateTime uploadedAt = LocalDateTime.now() ; 
    @ManyToOne
    @JoinColumn(name = "profile_id", nullable = false, updatable = false)
    private Profile profile ; 




    
}

/*

### Attributes

| Field      | Type          | Description        |
| ---------- | ------------- | -----------------  |
| resumeId   | UUID          | Unique identifier  |
| fileName   | String        | Resume file name   | 
| fileUrl    | String        | Storage location   |
| uploadedAt | LocalDateTime | Upload timestamp   |
| profile    | Profile       | profile associated |

---

*/