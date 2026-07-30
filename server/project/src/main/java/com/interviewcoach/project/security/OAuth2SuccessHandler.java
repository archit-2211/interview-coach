package com.interviewcoach.project.security;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.interviewcoach.project.auth.AuthService;

import com.interviewcoach.project.auth.refreshtoken.RefreshTokenService;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class OAuth2SuccessHandler
        implements AuthenticationSuccessHandler {

    private AuthService authService;
    private JwtService jwtService;
    private RefreshTokenService rtservice;

    public OAuth2SuccessHandler(AuthService authService, JwtService jwtService, RefreshTokenService rtservice) {
        this.authService = authService;
        this.jwtService = jwtService;
        this.rtservice = rtservice;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String firstName = oauthUser.getAttribute("given_name");
        String lastName = oauthUser.getAttribute("family_name");

        User savedUser = authService.handleGoogleLogin(email, firstName, lastName);
        String bearerToken = jwtService.generateToken(savedUser);
       

        if(savedUser.getUserRole().equals(UserRole.PENDING)) {
             response.sendRedirect("http://localhost:5173/role/me/"+bearerToken);  

        }
        else {
             String refreshToken = rtservice.generateToken(savedUser);
        

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken).path("/").httpOnly(true).secure(false).maxAge(60*60*24*30).sameSite("Lax").build() ; 
        
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.sendRedirect("http://localhost:5173/auth/success#token="+bearerToken);

        }
        
    
        
         
    

 

    }
}