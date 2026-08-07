package com.interviewcoach.project.InterviewManagement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import com.interviewcoach.project.InterviewManagement.dtos.FeedbackDTO;
import com.interviewcoach.project.enums.InterviewStatus;
import com.interviewcoach.project.models.CandidatesAssessment;
import com.interviewcoach.project.models.Feedback;
import com.interviewcoach.project.models.Interview;
import com.interviewcoach.project.models.InterviewRequest;
import com.interviewcoach.project.models.InterviewersFeedback;
import com.interviewcoach.project.models.Profile;
import com.interviewcoach.project.security.JwtService;


@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {
    @Mock
    private FeedbackRepository feedbackRepository;
    @Mock
    private InterviewManagementService imService;

    @Mock
    private JwtService jwtService ; 
  
    @InjectMocks
    FeedbackService feedbackService;

    private UUID interviewId;
    private FeedbackDTO dto;
    private String email;



    @BeforeEach
    void setup() {
        interviewId = UUID.randomUUID();
        dto = new FeedbackDTO("Excellent Interview", 5);
        email = "candidate@test.com";
    }

    @Test
    void shouldThrowExceptionWhenInterviewNotCompleted() {

        when(jwtService.isInterviewer()).thenReturn(false);

        Interview interview = createCompletedInterview();
        interview.setInterviewStatus(InterviewStatus.SCHEDULED);

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> feedbackService.addFeedback(interviewId, dto, email));

        assertEquals(
                "Feedback can only be submitted after interview completion",
                exception.getMessage());

        verify(feedbackRepository, never()).save(any());
        verify(imService, never()).markFeedbackSubmitted(any());
        verify(imService, never()).markAssessmentSubmitted(any());
    }

    @Test
    void shouldSubmitCandidateFeedbackSuccessfully() {

        when(jwtService.isInterviewer()).thenReturn(false);

        Interview interview = createCompletedInterview();

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        when(feedbackRepository.save(any(Feedback.class)))
                .thenReturn(interview.getFeedback());

        String result =
                feedbackService.addFeedback(interviewId, dto, email);

        assertEquals("SUCCESS", result);

        assertTrue(interview.isFeedbackSubmitted());

        InterviewersFeedback feedback =
                interview.getFeedback().getInterviewersFeedback();

        assertEquals(5, feedback.getInterviewerRating());
        assertEquals("Excellent Interview", feedback.getComments());

        verify(imService).validateProfile(
                interview.getInterviewRequest().getCandidateProfile(),
                email);

        verify(imService).markFeedbackSubmitted(interview);

        verify(feedbackRepository).save(interview.getFeedback());

        verify(imService, never()).markAssessmentSubmitted(any());
    }

    @Test
    void shouldSubmitInterviewerAssessmentSuccessfully() {

        when(jwtService.isInterviewer()).thenReturn(true);

        Interview interview = createCompletedInterview();

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        when(feedbackRepository.save(any(Feedback.class)))
                .thenReturn(interview.getFeedback());

        String result =
                feedbackService.addFeedback(interviewId, dto, email);

        assertEquals("SUCCESS", result);

        assertTrue(interview.isAssessmentSubmitted());

        CandidatesAssessment assessment =
                interview.getFeedback().getCandidatesAssessment();

        assertEquals(5, assessment.getCandidateRating());
        assertEquals("Excellent Interview", assessment.getAssessment());

        verify(imService).validateProfile(
                interview.getInterviewRequest().getInterviewerProfile(),
                email);

        verify(imService).markAssessmentSubmitted(interview);

        verify(feedbackRepository).save(interview.getFeedback());

        verify(imService, never()).markFeedbackSubmitted(any());
    }

    @Test
    void shouldThrowExceptionWhenCandidateValidationFails() {

        when(jwtService.isInterviewer()).thenReturn(false);

        Interview interview = createCompletedInterview();

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        doThrow(new RuntimeException("Invalid Profile"))
                .when(imService)
                .validateProfile(any(Profile.class), anyString());

        assertThrows(
                RuntimeException.class,
                () -> feedbackService.addFeedback(interviewId, dto, email));

        verify(feedbackRepository, never()).save(any());
        verify(imService, never()).markFeedbackSubmitted(any());
    }

    @Test
    void shouldThrowExceptionWhenInterviewerValidationFails() {

        when(jwtService.isInterviewer()).thenReturn(true);

        Interview interview = createCompletedInterview();

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        doThrow(new RuntimeException("Invalid Profile"))
                .when(imService)
                .validateProfile(any(Profile.class), anyString());

        assertThrows(
                RuntimeException.class,
                () -> feedbackService.addFeedback(interviewId, dto, email));

        verify(feedbackRepository, never()).save(any());
        verify(imService, never()).markAssessmentSubmitted(any());
    }

    @Test
    void shouldPropagateRepositoryException() {

        when(jwtService.isInterviewer()).thenReturn(false);

        Interview interview = createCompletedInterview();

        when(imService.getInterviewById(interviewId))
                .thenReturn(interview);

        when(feedbackRepository.save(any(Feedback.class)))
                .thenThrow(new RuntimeException("Database Error"));

        assertThrows(
                RuntimeException.class,
                () -> feedbackService.addFeedback(interviewId, dto, email));

        verify(feedbackRepository).save(any(Feedback.class));
    }

    private Interview createCompletedInterview() {

        Profile candidate = new Profile();
        Profile interviewer = new Profile();

        InterviewRequest request = new InterviewRequest();
        request.setCandidateProfile(candidate);
        request.setInterviewerProfile(interviewer);

        InterviewersFeedback interviewerFeedback =
                new InterviewersFeedback();

        CandidatesAssessment candidatesAssessment =
                new CandidatesAssessment();

        Feedback feedback = new Feedback();
        feedback.setInterviewersFeedback(interviewerFeedback);
        feedback.setCandidatesAssessment(candidatesAssessment);

        Interview interview = new Interview();
        interview.setInterviewRequest(request);
        interview.setFeedback(feedback);
        interview.setInterviewStatus(InterviewStatus.COMPLETED);

        return interview;
    }

}
