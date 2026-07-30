package com.interviewcoach.project.SlotManagement;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.enums.SlotStatus;
import com.interviewcoach.project.models.Slot;


import java.util.List;


public interface SlotRepository extends JpaRepository<Slot, UUID> {

    Page<Slot> findByProfileProfileIdAndSlotStatusOrderBySlotTimingDateAsc(UUID profileId, SlotStatus status, Pageable page);
    List<Slot> findByProfileUserEmail(String email );
    List<Slot> findByProfileUserEmailAndSlotStatus(String email , SlotStatus slotStatus);
}
