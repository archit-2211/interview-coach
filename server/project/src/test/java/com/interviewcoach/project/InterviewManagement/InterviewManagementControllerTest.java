package com.interviewcoach.project.InterviewManagement;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.interviewcoach.project.InterviewManagement.dtos.InterviewDTO;

import com.interviewcoach.project.InterviewManagement.dtos.InterviewerResponseDTO;


@WebMvcTest(InterviewManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InterviewManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InterviewManagementService imService;

    @MockitoBean
    private FeedbackService feedbackService;

    /*
     * Your application has JwtAuthenticationFilter.
     * If JwtService is required while loading the test context,
     * mock it as well.
     */
    @MockitoBean
    private com.interviewcoach.project.security.JwtService jwtService;

    private Authentication authentication;

    private final String email = "candidate@gmail.com";

    @BeforeEach
    void setUpSecurityContext() {

        authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }


    // =========================================================
    // GET /interviews/interviewers
    // =========================================================

   @Test
void getInterviewers_success() throws Exception {

    List<InterviewerResponseDTO> interviewers = List.of();

    when(imService.getInterviewers(
            List.of("Java", "Spring"),
            0,
            10
    )).thenReturn(interviewers);

    mockMvc.perform(
            get("/interviews/interviewers")
                    .param("skills", "Java", "Spring")
                    .param("pageNumber", "0")
                    .param("pageSize", "10")
    )
    .andExpect(status().isOk());

    verify(imService)
            .getInterviewers(
                    List.of("Java", "Spring"),
                    0,
                    10
            );
}


    @Test
    void getInterviewers_defaultPagination() throws Exception {

        when(imService.getInterviewers(
                List.of("Java"),
                0,
                10
        )).thenReturn(List.of());

        mockMvc.perform(
                get("/interviews/interviewers")
                        .param("skills", "Java")
        )
        .andExpect(status().isOk());

        verify(imService)
                .getInterviewers(
                        List.of("Java"),
                        0,
                        10
                );
    }


    // =========================================================
    // GET /interviews/requests/me
    // =========================================================

    @Test
    void getAllInterviewRequests_success() throws Exception {

        when(imService.getAllInterviewRequests(email))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/interviews/requests/me")
        )
        .andExpect(status().isOk());

        verify(imService)
                .getAllInterviewRequests(email);
    }


    // =========================================================
    // GET /interviews/requests/pending
    // =========================================================

    @Test
    void getPendingInterviewRequests_success() throws Exception {

        when(imService.getPendingInterviewRequests(email))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/interviews/requests/pending")
        )
        .andExpect(status().isOk());

        verify(imService)
                .getPendingInterviewRequests(email);
    }


    // =========================================================
    // PUT /interviews/requests/{id}/accept
    // =========================================================

    @Test
    void acceptInterviewRequest_success() throws Exception {

        UUID requestId = UUID.randomUUID();

        InterviewDTO response = mock(InterviewDTO.class);

        when(imService.acceptInterviewRequest(
                email,
                requestId,
                "https://meeting.com/demo"
        )).thenReturn(response);

        mockMvc.perform(
                put("/interviews/requests/{id}/accept", requestId)
                        .param(
                                "meetingLink",
                                "https://meeting.com/demo"
                        )
        )
        .andExpect(status().isOk());

        verify(imService)
                .acceptInterviewRequest(
                        email,
                        requestId,
                        "https://meeting.com/demo"
                );
    }


    // =========================================================
    // PUT /interviews/requests/{id}/reject
    // =========================================================

    @Test
    void rejectInterviewRequest_success() throws Exception {

        UUID requestId = UUID.randomUUID();

        when(imService.rejectInterviewRequest(
                email,
                requestId
        )).thenReturn(
                "Interview request rejected successfully"
        );

        mockMvc.perform(
                put("/interviews/requests/{id}/reject", requestId)
        )
        .andExpect(status().isOk());

        verify(imService)
                .rejectInterviewRequest(
                        email,
                        requestId
                );
    }


    // =========================================================
    // PUT /interviews/requests/{id}/cancel
    // =========================================================

    @Test
    void cancelInterviewRequest_success() throws Exception {

        UUID requestId = UUID.randomUUID();

        when(imService.cancelInterviewRequest(
                email,
                requestId
        )).thenReturn("SUCCESS");

        mockMvc.perform(
                put("/interviews/requests/{id}/cancel", requestId)
        )
        .andExpect(status().isOk());

        verify(imService)
                .cancelInterviewRequest(
                        email,
                        requestId
                );
    }


    // =========================================================
    // GET /interviews/me
    // =========================================================

    @Test
    void getMyInterviews_success() throws Exception {

        when(imService.getMyInterviews(email))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/interviews/me")
        )
        .andExpect(status().isOk());

        verify(imService)
                .getMyInterviews(email);
    }


    // =========================================================
    // PUT /interviews/{id}/complete
    // =========================================================

    @Test
    void completeInterview_success() throws Exception {

        UUID interviewId = UUID.randomUUID();

        InterviewDTO response = mock(InterviewDTO.class);

        when(imService.completeInterview(
                interviewId,
                email
        )).thenReturn(response);

        mockMvc.perform(
                put("/interviews/{id}/complete", interviewId)
        )
        .andExpect(status().isOk());

        verify(imService)
                .completeInterview(
                        interviewId,
                        email
                );
    }


    // =========================================================
    // PUT /interviews/{id}/cancel
    // =========================================================

    @Test
    void cancelInterview_success() throws Exception {

        UUID interviewId = UUID.randomUUID();

        when(imService.cancelInterview(
                interviewId,
                email
        )).thenReturn("SUCCESS");

        mockMvc.perform(
                put("/interviews/{id}/cancel", interviewId)
        )
        .andExpect(status().isOk());

        verify(imService)
                .cancelInterview(
                        interviewId,
                        email
                );
    }


    // =========================================================
    // Basic security-context verification
    // =========================================================

    @Test
    void controllerGetsEmailFromSecurityContext() throws Exception {

        when(imService.getAllInterviewRequests(email))
                .thenReturn(List.of());

        mockMvc.perform(
                get("/interviews/requests/me")
        )
        .andExpect(status().isOk());

        verify(authentication)
                .getName();

        verify(imService)
                .getAllInterviewRequests(email);
    }
}