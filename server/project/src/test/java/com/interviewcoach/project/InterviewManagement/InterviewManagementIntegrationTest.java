package com.interviewcoach.project.InterviewManagement;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InterviewManagementIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    @WithMockUser(
            username = "candidate@gmail.com",
            authorities = "ROLE_CANDIDATE"
    )
    void getInterviewers_success() throws Exception {

        mockMvc.perform(
                get("/interviews/interviewers")
                        .param("skills", "Java", "Spring")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
        )
        .andExpect(status().isOk());
    }

  
    @Test
    @WithMockUser(
            username = "candidate@gmail.com",
            authorities = "ROLE_CANDIDATE"
    )
    void getMyInterviewRequests_success() throws Exception {

        mockMvc.perform(
                get("/interviews/requests/me")
        )
        .andExpect(status().isOk());
    }



    @Test
    @WithMockUser(
            username = "interviewer@gmail.com",
            authorities = "ROLE_INTERVIEWER"
    )
    void getPendingInterviewRequests_success() throws Exception {

        mockMvc.perform(
                get("/interviews/requests/pending")
        )
        .andExpect(status().isOk());
    }

  
    @Test
    @WithMockUser(
            username = "candidate@gmail.com",
            authorities = "ROLE_CANDIDATE"
    )
    void getMyInterviews_success() throws Exception {

        mockMvc.perform(
                get("/interviews/me")
        )
        .andExpect(status().isOk());
    }


    @Test
    void getInterviewers_withoutAuthentication() throws Exception {

        mockMvc.perform(
                get("/interviews/interviewers")
                        .param("skills", "Java")
        )
        .andExpect(status().isUnauthorized());
    }

    @Test
    void getMyInterviews_withoutAuthentication() throws Exception {

        mockMvc.perform(
                get("/interviews/me")
        )
        .andExpect(status().isUnauthorized());
    }
}