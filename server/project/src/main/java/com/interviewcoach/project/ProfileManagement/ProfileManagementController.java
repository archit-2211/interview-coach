package com.interviewcoach.project.ProfileManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.project.ProfileManagement.DTO.ProfileResponseDTO;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileSkillsDTO;

import com.interviewcoach.project.ProfileManagement.DTO.WorkexCreationDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexResponseDTO;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/profiles")
@SecurityRequirement(name = "bearerAuth")
public class ProfileManagementController {

    private final ProfileManagementService profileService;

    public ProfileManagementController(ProfileManagementService profileService) {
        this.profileService = profileService;

    }

    @GetMapping("/interviewer")
    public  ResponseEntity<ProfileResponseDTO> getProfileUsingEmail(@RequestParam String email) {
        System.out.println("Got the request for profile using email \n\n\n\n");
        return ResponseEntity.ok(profileService.getProfileByEmail(email));

        
    }
    

    @GetMapping("/me")

    public ResponseEntity<ProfileResponseDTO> profileDisplay(){
        String userEmail = getEmail() ; 
        return ResponseEntity.ok(profileService.getProfile(userEmail));

    }

    @PostMapping("/me/skills")
    public ResponseEntity<ProfileSkillsDTO> updateSkills(
            @RequestBody ProfileSkillsDTO skillNames) {

        String userEmail = getEmail() ; 

        return ResponseEntity.ok(
                profileService.updateSkills(
                        skillNames.skills(),
                        userEmail));
    }

    @GetMapping("/me/work-experiences")
    public ResponseEntity<WorkexResponseDTO> getExperiences() {
        String email = getEmail() ; 
        List<WorkexDTO> experiences = profileService.getWorkExperiences(email) ; 
        WorkexResponseDTO responsePayload = new WorkexResponseDTO(experiences) ; 
        return ResponseEntity.ok(responsePayload) ; 

        
    }

    @GetMapping("/work-experiences")
    public ResponseEntity<WorkexResponseDTO>  getWorkExperiences(@RequestParam String email) {
        List<WorkexDTO> experiences = profileService.getWorkExperiences(email) ; 
        WorkexResponseDTO responsePayload = new WorkexResponseDTO(experiences) ; 
        return ResponseEntity.ok(responsePayload) ; 
        
    }
    
    
    

    @PostMapping("/me/work-experiences")
    public ResponseEntity<WorkexResponseDTO> addWorkExperience(@RequestBody WorkexCreationDTO workexDetails) {
        String email = getEmail();

        System.out.println("Hitting the api now ");
        
        WorkexResponseDTO response = profileService.addWorkex(email, workexDetails) ; 
        return ResponseEntity.ok(response) ; 

    }

    @DeleteMapping("/me/work-experiences/{id}")
    public ResponseEntity<String> removeWorkExperience(@PathVariable UUID id) {
       String email = getEmail() ; 
       profileService.deleteWorkex(id, email);
       return ResponseEntity.ok("SUCCESS");
    }   

    // @PutMapping("/me/work-experiences/{id}")
    // public ResponseEntity<WorkexResponseDTO> updateWorkex(@PathVariable UUID id, @RequestBody WorkexDTO dto) {
    //     String email = getEmail() ; 
    //     return ResponseEntity.ok(profileService.updateWorkex(id,dto, email));

      
    // }


    private String getEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName() ; 
    }


}



