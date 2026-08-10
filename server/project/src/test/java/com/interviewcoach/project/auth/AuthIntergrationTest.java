package com.interviewcoach.project.auth;

import static org.hamcrest.Matchers.containsString;
import org.springframework.http.HttpHeaders;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class AuthIntergrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void register_success() throws Exception {
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name" : "someone",
                                    "phoneNumber" : "9898484838" ,
                                    "email" : "someone@email.com" ,
                                    "password" : "passwordpassword" ,
                                    "role" : "CANDIDATE"
                                }

                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("someone@email.com"))
                .andExpect(jsonPath("$.success").value(true));

    }

    @Test
    void register_duplicateEmail() throws Exception {

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "someone",
                                    "phoneNumber": "9898484838",
                                    "email": "duplicate@email.com",
                                    "password": "passwordpassword",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "another",
                                    "phoneNumber": "9876543210",
                                    "email": "duplicate@email.com",
                                    "password": "passwordpassword",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void login_success() throws Exception {

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "loginuser",
                                    "phoneNumber": "9898484838",
                                    "email": "login@email.com",
                                    "password": "passwordpassword",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "login@email.com",
                                    "password": "passwordpassword"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(header().string(
                        HttpHeaders.SET_COOKIE,
                        containsString("refreshToken=")));
    }

    @Test
    void login_invalidCredentials() throws Exception {

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "doesnotexist@email.com",
                                    "password": "wrongpassword"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void refreshToken_success() throws Exception {

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "refreshuser",
                                    "phoneNumber": "9898484838",
                                    "email": "refresh@email.com",
                                    "password": "passwordpassword",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isOk());

        var loginResult = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email": "refresh@email.com",
                                    "password": "passwordpassword"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn();

        String setCookie = loginResult.getResponse()
                .getHeader(HttpHeaders.SET_COOKIE);

        String refreshToken = setCookie
                .split(";")[0]
                .substring("refreshToken=".length());

        mockMvc.perform(
                post("/auth/refresh")
                        .cookie(
                                new Cookie(
                                        "refreshToken",
                                        refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void refreshToken_withoutCookie() throws Exception {

        mockMvc.perform(
                post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

}
