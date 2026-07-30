package com.interviewcoach.project.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import com.interviewcoach.project.enums.RefreshTokenStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshToken {


    @Id
    private UUID tokenValue ;
    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "userId", nullable = false)
    private User user ; 
    @Builder.Default
    private Instant createdAt = Instant.now(); 
    @Builder.Default
    private Instant expiryAt = Instant.now().plus(30, ChronoUnit.DAYS) ; 
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RefreshTokenStatus tokenStatus = RefreshTokenStatus.Active; 
    
}
