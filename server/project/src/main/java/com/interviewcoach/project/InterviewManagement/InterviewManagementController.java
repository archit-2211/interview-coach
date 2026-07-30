package com.interviewcoach.project.InterviewManagement;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.project.InterviewManagement.dtos.AllInterviewsDTO;
import com.interviewcoach.project.InterviewManagement.dtos.FeedbackDTO;
import com.interviewcoach.project.InterviewManagement.dtos.FeedbackResponseDTO;
import com.interviewcoach.project.InterviewManagement.dtos.IRDetailsDTO;
import com.interviewcoach.project.InterviewManagement.dtos.InterviewDTO;
import com.interviewcoach.project.InterviewManagement.dtos.InterviewRequestDTO;

import com.interviewcoach.project.InterviewManagement.dtos.InterviewersDTO;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/interviews")
@SecurityRequirement(name = "bearerAuth")
public class InterviewManagementController {

    private InterviewManagementService imService;
    private FeedbackService feedbackService ; 

    public InterviewManagementController(InterviewManagementService imService, FeedbackService feedbackService) {
        this.imService = imService;
        this.feedbackService = feedbackService ; 

    }

    @GetMapping("/interviewers")
    public ResponseEntity<InterviewersDTO> getInterviewers(
            @RequestParam List<String> skills,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(new InterviewersDTO(imService.getInterviewers(skills, pageNumber, pageSize)));
    }



    

    @PostMapping("/requests")
    public ResponseEntity<String> raiseInterviewRequest(@RequestBody IRDetailsDTO request) {
         
        String userEmail = getEmail();


        imService.createInterviewRequest(request, userEmail);
        return ResponseEntity.ok("Done");



        /*
        Just get the interview slot id and do the creating request flow
        Changes has to be made excessive dto attributes provided 
        */
    }

    @GetMapping("/requests/me")
    public ResponseEntity<List<InterviewRequestDTO>> getAllInterviewRequests() {
        String email = getEmail();
        return ResponseEntity.ok(imService.getAllInterviewRequests(email));

    }

    @GetMapping("/requests/pending")
    public ResponseEntity<List<InterviewRequestDTO>> getPendingInterviewRequests() {
        String email = getEmail();
        return ResponseEntity.ok(imService.getPendingInterviewRequests(email));

    }

    @PutMapping("/requests/{id}/accept")
    public ResponseEntity<InterviewDTO> acceptInterviewRequest(@PathVariable UUID id, @RequestParam String meetingLink) {
        System.out.println("\n\n\n debugg \n\n\n");
        String email = getEmail();
        return ResponseEntity.ok(imService.acceptInterviewRequest(email, id, meetingLink));

    }

    @PutMapping("/requests/{id}/reject")
    public ResponseEntity<String> rejectInterviewRequest(@PathVariable UUID id) {
        String email = getEmail();
        return ResponseEntity.ok(imService.rejectInterviewRequest(email, id));

    }

    @PutMapping("/requests/{id}/cancel")
    public ResponseEntity<String> cancelInterviewRequest(@PathVariable UUID id) {
        String email = getEmail();
        return ResponseEntity.ok(imService.cancelInterviewRequest(email, id));

    }

    @GetMapping("/me")
    public ResponseEntity<AllInterviewsDTO> getMyInterviews() {
        String email = getEmail();
        return ResponseEntity.ok(new AllInterviewsDTO(imService.getMyInterviews(email)));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<InterviewDTO> completeInterview(@PathVariable UUID id) {
        String email = getEmail();
        return ResponseEntity.ok(imService.completeInterview(id, email));

        /*
        
        At complete interview increment the total interview count of candidate and interviewer
        
        
        */

    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<String> cancelInterview(@PathVariable UUID id) {
        String email = getEmail();
        return ResponseEntity.ok(imService.cancelInterview(id, email));

    }

    

    @PostMapping("/{interviewId}/feedback")
    public ResponseEntity<String> addFeedback( @PathVariable UUID interviewId ,@RequestBody FeedbackDTO request) {

        System.out.println("\n\n\nRequest Received here \n\n\n") ; 
        String email = getEmail() ; 
        return ResponseEntity.ok(feedbackService.addFeedback(interviewId, request, email));
    }

    // @GetMapping("/{interviewId}/feedback")
    // public ResponseEntity<FeedbackResponseDTO> getFeedback(@PathVariable UUID interviewId) {
    //     String email = getEmail() ; 
    //     return ResponseEntity.ok(feedbackService.getFeedback(interviewId, email)) ; 
    // }

    

    private String getEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();

    }

}
