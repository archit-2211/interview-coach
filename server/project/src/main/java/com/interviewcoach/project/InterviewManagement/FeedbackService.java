package com.interviewcoach.project.InterviewManagement;


import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.InterviewManagement.dtos.FeedbackDTO;
import com.interviewcoach.project.InterviewManagement.dtos.FeedbackResponseDTO;
import com.interviewcoach.project.enums.InterviewStatus;
import com.interviewcoach.project.models.CandidatesAssessment;
import com.interviewcoach.project.models.Feedback;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewersFeedback;
import com.interviewcoach.project.models.Profile;

@Service
public class FeedbackService {
    private FeedbackRepository feedbackRepository ; 
    private InterviewManagementService imService ; 
    

    public FeedbackService(FeedbackRepository feedbackRepository, InterviewManagementService imService) {
        this.feedbackRepository = feedbackRepository ; 
        this.imService = imService ; 
    }

    public String addFeedback(UUID interviewId, FeedbackDTO dto, String email) {
        System.out.println("\n\n\n debugg \n\n\n") ; 
                boolean isInterviewer = isInterviewer();
                Interview interview = imService.getInterviewById(interviewId);

                Profile candidateProfile = interview.getInterviewRequest().getCandidateProfile();
                Profile interviewProfile = interview.getInterviewRequest().getInterviewerProfile();
                Feedback feedback = interview.getFeedback();
      

                if (interview.getInterviewStatus() != InterviewStatus.COMPLETED) {

                        throw new RuntimeException(
                                        "Feedback can only be submitted after interview completion");
                }
     
                if (isInterviewer) {
                        imService.validateProfile(interviewProfile, email);
        
                        CandidatesAssessment candidatesAssessment = feedback.getCandidatesAssessment(); 
                
                        candidatesAssessment.setCandidateRating(dto.rating());
               
                        candidatesAssessment.setAssessment(dto.description());
                        interview.setAssessmentSubmitted(true);
                        imService.markAssessmentSubmitted(interview);

                } else {
                        imService.validateProfile(candidateProfile, email);
                 System.out.println("\n\n\n debugg \n\n\n") ; 
                        InterviewersFeedback interviewersFeedback = feedback.getInterviewersFeedback() ; 
                 System.out.println("\n\n\n debugg \n\n\n") ; 
                        interviewersFeedback.setInterviewerRating(dto.rating());
                 System.out.println("\n\n\n debugg \n\n\n") ; 
                        interviewersFeedback.setComments(dto.description());
                         interview.setFeedbackSubmitted(true) ; 
                        imService.markFeedbackSubmitted(interview);
                      

                }
                feedback = feedbackRepository.save(feedback) ;
               
                


                return "SUCCESS" ; 

        }


    public FeedbackResponseDTO getFeedback(UUID interviewId, String email) {
                boolean isInterviewer = isInterviewer();
                Interview interview = imService.getInterviewById(interviewId);
                Profile candidateProfile = interview.getInterviewRequest().getCandidateProfile();
                Profile interviewProfile = interview.getInterviewRequest().getInterviewerProfile();
                Feedback feedback = interview.getFeedback();

                return null ; 

        }
        protected boolean isInterviewer() {
              Authentication auth = SecurityContextHolder
                                .getContext()
                                .getAuthentication();

                boolean isInterviewer = auth.getAuthorities()
                                .stream()
                                .anyMatch(
                                                authority -> authority.getAuthority()
                                                                .equals("ROLE_INTERVIEWER"));
                return isInterviewer;

        }

}
