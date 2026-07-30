package com.interviewcoach.project.SlotManagement;

import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.project.SlotManagement.dtos.AllSlotsDTO;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/slots")
@SecurityRequirement(name = "bearerAuth")
public class SlotController {

    private final SlotService slotService;

    public SlotController(
            SlotService slotService
    ) {
        this.slotService = slotService;


    }

    @PostMapping("/me")
    public ResponseEntity<AllSlotsDTO> createSlot(
            @RequestBody AllSlotsDTO dto
    ) {

        return ResponseEntity.ok(new AllSlotsDTO(
                slotService.addSlot(
                        getEmail(),
                        dto.slots()
                ))
        );
    }

     @GetMapping("/get")
    public ResponseEntity<AllSlotsDTO> getSlots(@RequestParam String email) {
  

        return ResponseEntity.ok(
                new AllSlotsDTO(slotService.getActiveSlots(
                        email
                ))
        );
    }




    @GetMapping("/me")
    public ResponseEntity<AllSlotsDTO> getMySlots() {
        System.out.println("Request received to get slots \n \n \n") ; 

        return ResponseEntity.ok(
                new AllSlotsDTO(slotService.getMySlots(
                        getEmail()
                ))
        );
    }

//     @PutMapping("/{slotId}")
//     public ResponseEntity<SlotDTO> updateSlot(
//             @PathVariable UUID slotId,
//             @RequestBody SlotDTO dto
//     ) {

//         return ResponseEntity.ok(
//                 slotService.updateSlot(
//                         slotId,
//                         dto,
//                         getEmail()
//                 )
//         );
//     }

    @DeleteMapping("/{slotId}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable UUID slotId
    ) {

        slotService.deleteSlot(
                slotId,
                getEmail()
        );

        return ResponseEntity.ok()
                .build();
    }

    private String getEmail() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
