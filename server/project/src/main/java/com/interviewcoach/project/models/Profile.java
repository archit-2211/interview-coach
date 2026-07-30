package com.interviewcoach.project.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter 
@ToString
public class Profile {

    @Id
    private UUID profileId ; 
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user  ; 
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resume> resumes = new ArrayList<>()  ; 
   
    @ManyToMany
    @JoinTable(name = "profile_skills", joinColumns = @JoinColumn(name = "profile_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
    private List<Skill> skills = new ArrayList<>() ;  ; 
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkExperience> workExperiences = new ArrayList<>() ; 
    private double rating; 
    private int totalInterviewsAttended ; 
     

    
    
}


/*

## Profile

Represents user profile information shared across candidates and interviewers.

### Attributes

| Field           | Type                 | Description             |
| --------------- | -------------------- | ----------------------- |
| profileId       | UUID                 | Unique identifier       |
| user            | User                 | Associated user         |
| resumes         | List<Resume>         | Uploaded resumes        |
| skills          | List<Skill>          | User skills             |
| workExperiences | List<WorkExperience> | Professional experience |
| rating          | Double              | Profile rating          |

---
*/