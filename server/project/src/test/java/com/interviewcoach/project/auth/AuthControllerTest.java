package com.interviewcoach.project.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.interviewcoach.project.auth.dto.LoggedinDTO;
import com.interviewcoach.project.auth.dto.LoginDTO;
import com.interviewcoach.project.auth.dto.RegisterDTO;
import com.interviewcoach.project.auth.dto.RegisteredDTO;
import com.interviewcoach.project.auth.dto.RoleDTO;
import com.interviewcoach.project.auth.dto.RoleSetupDTO;
import com.interviewcoach.project.security.JwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @MockitoBean
    private AuthService authService;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_success() throws Exception {

        RegisteredDTO response = new RegisteredDTO(
                "archit@gmail.com",
                false,
                true);
        when(authService.register(any(RegisterDTO.class))).thenReturn(response);

        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Archit",
                                    "phoneNumber": "9876543210",
                                    "email": "archit@gmail.com",
                                    "password": "password123",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("archit@gmail.com"))
                .andExpect(jsonPath("$.verified")
                        .value(false))
                .andExpect(jsonPath("$.success")
                        .value(true));

        verify(authService)
                .register(any(RegisterDTO.class));

    }

    @Test
    void register_failInvalidEmail() throws Exception {
        mockMvc.perform(
                post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "",
                                    "phoneNumber": "94595969594",
                                    "email": "invalid",
                                    "password": "password",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isBadRequest());

        verify(authService, never())
                .register(any(RegisterDTO.class));

    }

    /*
     * Similar tests can be written for invalid phoneNumbeer etc
     */

    @Test
    void login_sucess() throws Exception {

        LoggedinDTO serviceResponse = new LoggedinDTO("demobearertoken", "demorefreshtoken");

        when(authService.login(any(LoginDTO.class))).thenReturn(serviceResponse);

        mockMvc.perform(

                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                        "email" : "demo@gmail.com" , "password" : "demodemo"


                                    }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(serviceResponse.bearerToken()))
                .andExpect(header().string(
                        HttpHeaders.SET_COOKIE,
                        org.hamcrest.Matchers.containsString(
                                "refreshToken=" + serviceResponse.refreshToken())));

        verify(authService).login(any(LoginDTO.class));

    }

    @Test
    void login_serviceThrowsException() {

        when(authService.login(any(LoginDTO.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        ServletException exception = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "email": "demo@gmail.com",
                                            "password": "wrongpassword"
                                        }
                                        """)));

        assertInstanceOf(
                RuntimeException.class,
                exception.getCause());

        assertEquals(
                "Invalid credentials",
                exception.getCause().getMessage());

        verify(authService)
                .login(any(LoginDTO.class));
    }

    @Test
    void refreshToken_success() throws Exception {

        LoggedinDTO serviceResponse = new LoggedinDTO(
                "new-bearer-token",
                "refresh-token");

        when(authService.reIssueToken("refresh-token"))
                .thenReturn(serviceResponse);

        mockMvc.perform(
                post("/auth/refresh")
                        .cookie(
                                new Cookie(
                                        "refreshToken",
                                        "refresh-token")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value("new-bearer-token"));

        verify(authService)
                .reIssueToken("refresh-token");
    }

    @Test
    void refreshToken_missingCookie() throws Exception {

        ServletException exception = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(
                        post("/auth/refresh")));

        assertEquals(
                "Invalid RefreeshGToken",
                exception.getCause().getMessage());

        verify(authService, never())
                .reIssueToken(anyString());
    }

    @Test
    void logout_success() throws Exception {

        String refreshToken = "refresh-token-123";

        mockMvc.perform(
                get("/auth/logout")
                        .cookie(
                                new Cookie(
                                        "refreshToken",
                                        refreshToken)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string("Log out has been successful"))
                .andExpect(header().string(
                        HttpHeaders.SET_COOKIE,
                        org.hamcrest.Matchers.allOf(
                                org.hamcrest.Matchers.containsString("refreshToken="),
                                org.hamcrest.Matchers.containsString("Max-Age=0"),
                                org.hamcrest.Matchers.containsString("HttpOnly"),
                                org.hamcrest.Matchers.containsString("Path=/"))));

        verify(authService)
                .logOut(refreshToken);
    }

    @Test
    void logout_serviceThrowsException() throws Exception {

        String refreshToken = "refresh-token-123";

        doThrow(new RuntimeException("Invalid refresh token"))
                .when(authService)
                .logOut(refreshToken);

        assertThrows(
                ServletException.class,
                () -> mockMvc.perform(
                        get("/auth/logout")
                                .cookie(
                                        new Cookie(
                                                "refreshToken",
                                                refreshToken))));

        verify(authService)
                .logOut(refreshToken);
    }

    @Test
    void roleSetup_success() throws Exception {

        String serviceResponse = "ROLE SETUP SUCCESS";

        when(authService.roleSetup(any(RoleSetupDTO.class)))
                .thenReturn(serviceResponse);

        mockMvc.perform(
                post("/auth/role/setup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "jwtToken": "demo-jwt-token",
                                    "role": "CANDIDATE"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string(serviceResponse));

        ArgumentCaptor<RoleSetupDTO> captor = ArgumentCaptor.forClass(RoleSetupDTO.class);

        verify(authService)
                .roleSetup(captor.capture());

        RoleSetupDTO captured = captor.getValue();

        assertEquals(
                "demo-jwt-token",
                captured.jwtToken());

        assertEquals(
                RoleDTO.CANDIDATE,
                captured.role());
    }

}
