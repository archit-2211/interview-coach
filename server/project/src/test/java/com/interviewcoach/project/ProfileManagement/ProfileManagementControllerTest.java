package com.interviewcoach.project.ProfileManagement;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.interviewcoach.project.ProfileManagement.DTO.ProfileResponseDTO;
import com.interviewcoach.project.ProfileManagement.DTO.ProfileSkillsDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexCreationDTO;
import com.interviewcoach.project.ProfileManagement.DTO.WorkexResponseDTO;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.enums.UserStatus;
import com.interviewcoach.project.security.JwtService;

@WebMvcTest(ProfileManagementController.class)
@AutoConfigureMockMvc(addFilters = false)
class ProfileManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileManagementService profileService;

    @MockitoBean
    private JwtService jwtService;

    private Authentication authentication;

    private final String email = "candidate@gmail.com";

    @BeforeEach
    void setUp() {

        authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn(email);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }


    // =========================================================
    // GET /profiles/interviewer
    // =========================================================

    @Test
    void getProfileUsingEmail_success() throws Exception {

        ProfileResponseDTO response =
                new ProfileResponseDTO(
                        "interviewer@gmail.com",
                        "Interviewer",
                        "9876543210",
                        UserRole.INTERVIEWER,
                        UserStatus.VERIFIED,
                        8.5,
                        List.of("java", "spring")
                );

        when(profileService.getProfileByEmail(
                "interviewer@gmail.com"))
                .thenReturn(response);

        mockMvc.perform(
                get("/profiles/interviewer")
                        .param(
                                "email",
                                "interviewer@gmail.com"
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email")
                .value("interviewer@gmail.com"))
        .andExpect(jsonPath("$.name")
                .value("Interviewer"))
        .andExpect(jsonPath("$.rating")
                .value(8.5));

        verify(profileService)
                .getProfileByEmail(
                        "interviewer@gmail.com"
                );
    }


    // =========================================================
    // GET /profiles/me
    // =========================================================

    @Test
    void profileDisplay_success() throws Exception {

        ProfileResponseDTO response =
                new ProfileResponseDTO(
                        email,
                        "Archit",
                        "9876543210",
                        UserRole.CANDIDATE,
                        UserStatus.VERIFIED,
                        8.0,
                        List.of("java", "spring")
                );

        when(profileService.getProfile(email))
                .thenReturn(response);

        mockMvc.perform(
                get("/profiles/me")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.email")
                .value(email))
        .andExpect(jsonPath("$.name")
                .value("Archit"));

        verify(authentication)
                .getName();

        verify(profileService)
                .getProfile(email);
    }


    // =========================================================
    // POST /profiles/me/skills
    // =========================================================

    @Test
    void updateSkills_success() throws Exception {

        ProfileSkillsDTO response =
                new ProfileSkillsDTO(
                        List.of("java", "spring")
                );

        when(profileService.updateSkills(
                List.of("Java", "Spring"),
                email
        )).thenReturn(response);

        mockMvc.perform(
                post("/profiles/me/skills")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content("""
                                {
                                    "skills": [
                                        "Java",
                                        "Spring"
                                    ]
                                }
                                """)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.skills[0]")
                .value("java"))
        .andExpect(jsonPath("$.skills[1]")
                .value("spring"));

        verify(profileService)
                .updateSkills(
                        List.of("Java", "Spring"),
                        email
                );
    }


    // =========================================================
    // GET /profiles/me/work-experiences
    // =========================================================

    @Test
    void getExperiences_success() throws Exception {

        UUID workId = UUID.randomUUID();

        WorkexDTO workex =
                new WorkexDTO(
                        workId,
                        "Google",
                        java.time.LocalDate.of(2022, 1, 1),
                        java.time.LocalDate.of(2025, 1, 1)
                );

        when(profileService.getWorkExperiences(email))
                .thenReturn(List.of(workex));

        mockMvc.perform(
                get("/profiles/me/work-experiences")
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.experiences[0].id")
                .value(workId.toString()))
        .andExpect(jsonPath("$.experiences[0].companyName")
                .value("Google"));

        verify(profileService)
                .getWorkExperiences(email);
    }


    // =========================================================
    // GET /profiles/work-experiences
    // =========================================================

    @Test
    void getWorkExperiencesByEmail_success()
            throws Exception {

        String requestedEmail =
                "employee@gmail.com";

        UUID workId = UUID.randomUUID();

        WorkexDTO workex =
                new WorkexDTO(
                        workId,
                        "Microsoft",
                        java.time.LocalDate.of(2021, 1, 1),
                        java.time.LocalDate.of(2024, 1, 1)
                );

        when(profileService.getWorkExperiences(
                requestedEmail))
                .thenReturn(List.of(workex));

        mockMvc.perform(
                get("/profiles/work-experiences")
                        .param(
                                "email",
                                requestedEmail
                        )
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath(
                "$.experiences[0].companyName"
        ).value("Microsoft"));

        verify(profileService)
                .getWorkExperiences(requestedEmail);
    }


    // =========================================================
    // POST /profiles/me/work-experiences
    // =========================================================

    @Test
    void addWorkExperience_success()
            throws Exception {

        WorkexResponseDTO response =
                mock(WorkexResponseDTO.class);

        when(profileService.addWorkex(
                eq(email),
                any(WorkexCreationDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                post("/profiles/me/work-experiences")
                        .contentType(
                                MediaType.APPLICATION_JSON
                        )
                        .content("""
                                {
                                    "companyName": "Google",
                                    "startDate": "2022-01-01",
                                    "endDate": "2025-01-01"
                                }
                                """)
        )
        .andExpect(status().isOk());

        verify(profileService)
                .addWorkex(
                        eq(email),
                        any(WorkexCreationDTO.class)
                );
    }


    // =========================================================
    // DELETE /profiles/me/work-experiences/{id}
    // =========================================================

    @Test
    void removeWorkExperience_success()
            throws Exception {

        UUID workId = UUID.randomUUID();

        mockMvc.perform(
                delete(
                        "/profiles/me/work-experiences/{id}",
                        workId
                )
        )
        .andExpect(status().isOk())
        .andExpect(content().string("SUCCESS"));

        verify(profileService)
                .deleteWorkex(
                        workId,
                        email
                );
    }
}