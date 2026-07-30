package com.interviewcoach.project.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class CandidatesAssessment {
    @Min(value = 1)
    @Max(value = 5)
    int candidateRating; 
    String assessment ; 
    
}
