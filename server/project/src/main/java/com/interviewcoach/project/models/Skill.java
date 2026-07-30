package com.interviewcoach.project.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Skill {

    @Id
    private UUID skillId; 
    @NotBlank
    @Size(max = 50)
    @Column(unique = true, nullable = false)
    private String skillName ; 
    

    
    
}

/*


## Skill

Represents a technical or professional skill.

### Attributes

| Field     | Type   | Description       |
| --------- | ------ | ----------------- |
| skillId   | UUID   | Unique identifier |
| skillName | String | Skill name        |

*/