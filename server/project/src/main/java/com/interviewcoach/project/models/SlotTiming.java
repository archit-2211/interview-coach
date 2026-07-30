package com.interviewcoach.project.models;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class SlotTiming {

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;
}
/*


## SlotTiming

Represents a time window available for interviews.

### Attributes

| Field     | Type      | Description    |
| --------- | --------- | -------------- |
| date      | LocalDate | Interview date |
| startTime | LocalTime | Start time     |
| endTime   | LocalTime | End time       |

---

*/
