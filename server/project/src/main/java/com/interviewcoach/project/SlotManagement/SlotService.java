package com.interviewcoach.project.SlotManagement;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.SlotManagement.dtos.SlotDTO;
import com.interviewcoach.project.SlotManagement.exceptions.InvalidSlotException;
import com.interviewcoach.project.SlotManagement.exceptions.SlotNotFoundException;
import com.interviewcoach.project.SlotManagement.exceptions.SlotUnavailableException;
import com.interviewcoach.project.enums.SlotStatus;
import com.interviewcoach.project.GlobalExceptions.UnauthorisedException;
import com.interviewcoach.project.ProfileManagement.ProfileManagementService;

import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.models.Slot;
import com.interviewcoach.project.models.SlotTiming;

@Service

public class SlotService {

    private final SlotRepository slotRepository;
    private final ProfileManagementService pmService;

    public SlotService(
            SlotRepository slotRepository,
            ProfileManagementService pmService

    ) {
        
        this.slotRepository = slotRepository;
        this.pmService = pmService; 
    }

    @PreAuthorize("hasRole('INTERVIEWER')")
    public List<SlotDTO> addSlot(
            String email,
            List<SlotDTO> dto
    ) {

        slotValidation(dto);

        Profile profile = getProfileByEmail(email);

        List<Slot> slots = dto.stream()
                .map(slot -> mapDtoSlot(slot, profile))
                .toList();

        slots = slotRepository.saveAll(slots);

        return slots.stream()
                .map(this::mapSlotDto)
                .toList();
    }

    public List<SlotDTO> getMySlots(
            String email
    ) {
       
        List<SlotDTO> dto = slotRepository.findByProfileUserEmail(email)
                .stream()
                .map(this::mapSlotDto)
                .toList();
        return dto ; 

    }

    public List<SlotDTO> getActiveSlots(String email) {
        List<Slot> activeSlots = slotRepository.findByProfileUserEmailAndSlotStatus(email, SlotStatus.AVAILABLE) ; 
        return activeSlots.stream().map((ele) -> mapSlotDto(ele)).toList() ; 
        
    }
    @PreAuthorize("hasRole('INTERVIEWER')")
    public SlotDTO updateSlot(
            UUID slotId,
            SlotDTO dto,
            String email
    ) {

        slotValidation(List.of(dto));

        Profile profile = getProfileByEmail(email);

        Slot slot = slotRepository
                .findById(slotId)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Slot not found"
                        )
                );

        validateOwnership(slot, profile);

        SlotTiming timing = slot.getSlotTiming();

        timing.setDate(dto.date());
        timing.setStartTime(dto.startTime());
        timing.setEndTime(dto.endTime());

        slot.setSlotTiming(timing);

        slot = slotRepository.save(slot);

        return mapSlotDto(slot);
    }
    @PreAuthorize("hasRole('INTERVIEWER')")
    public void deleteSlot(
            UUID slotId,
            String email
    ) {
        System.out.println("Request received to delete slot t service \n \n") ;


        Slot slot = slotRepository.findById(slotId).orElseThrow(() -> new SlotNotFoundException("Slot Not Found")) ; 
        Profile profile = pmService.getProfileUsingEmail(email);
        validateOwnership(slot,profile );

        slotRepository.deleteById(slotId);

    }

    public Slot getSlotById(UUID slotId){
        return slotRepository.findById(slotId).orElseThrow((() -> new SlotNotFoundException("Slot Not Found"))) ; 
    }
    public Slot setSlotToBooked(Slot slot){
        if (!slot.getSlotStatus().equals(SlotStatus.AVAILABLE)){
                throw new SlotUnavailableException("Slot is not available to book");
        }
        slot.setSlotStatus(SlotStatus.BOOKED);
        return slotRepository.save(slot) ; 
    }

    private void validateOwnership(
            Slot slot,
            Profile profile
    ) {

        if (!slot.getProfile()
                .getProfileId()
                .equals(profile.getProfileId())) {

            throw new UnauthorisedException(
                    "Unauthorized access"
            );
        }
    }

    private void slotValidation(
            List<SlotDTO> dto
    ) {

        dto.forEach(slot -> {

            if (!slot.startTime()
                    .isBefore(slot.endTime())) {

                throw new InvalidSlotException(
                        "End time must be after start time"
                );
            }
        });
    }

    private Slot mapDtoSlot(
            SlotDTO dto,
            Profile profile
    ) {

        SlotTiming slotTiming = new SlotTiming();

        slotTiming.setDate(dto.date());
        slotTiming.setStartTime(dto.startTime());
        slotTiming.setEndTime(dto.endTime());

        return Slot.builder()
                .slotId(UUID.randomUUID())
                .profile(profile)
                .slotTiming(slotTiming)
                .build();
    }

    private Profile getProfileByEmail(
            String email
    ) {
        return pmService.getProfileUsingEmail(email);

    }

    private SlotDTO mapSlotDto(
            Slot slot
    ) {

        return new SlotDTO(
                slot.getSlotId(),
                slot.getSlotTiming().getDate(),
                slot.getSlotTiming().getStartTime(),
                slot.getSlotTiming().getEndTime()
        );
    }
}