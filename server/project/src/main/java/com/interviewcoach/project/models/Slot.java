package com.interviewcoach.project.models;

import java.util.UUID;

import com.interviewcoach.project.enums.SlotStatus;

import jakarta.persistence.Embedded;
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

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Slot {
    @Id
    private UUID slotId ; 
    @ManyToOne
    @JoinColumn(name =  "interviewer_profile_id", nullable = false)
    private Profile profile ; 
    @Embedded
    private SlotTiming slotTiming ; 
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private SlotStatus slotStatus = SlotStatus.AVAILABLE; 
    
}


/*

## Slot

Represents an interview availability slot created by an interviewer.

### Attributes

| Field       | Type       | Description                 |
| ----------- | ---------- | --------------------------- |
| slotId      | UUID       | Unique identifier           |
| interviewer | User       | Interviewer owning the slot |
| slotTiming  | SlotTiming | Slot schedule               |

---


*/