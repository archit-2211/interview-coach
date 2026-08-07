package com.interviewcoach.project.auth;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.interviewcoach.project.ProfileManagement.ProfileManagementService;

import com.interviewcoach.project.auth.dto.LoggedinDTO;
import com.interviewcoach.project.auth.dto.LoginDTO;

import com.interviewcoach.project.auth.dto.RegisterDTO;
import com.interviewcoach.project.auth.dto.RegisteredDTO;
import com.interviewcoach.project.auth.dto.RoleDTO;
import com.interviewcoach.project.auth.dto.RoleSetupDTO;
import com.interviewcoach.project.auth.exceptions.IncorrectCredentialsException;
import com.interviewcoach.project.auth.exceptions.InvalidRefreshTokenException;
import com.interviewcoach.project.auth.exceptions.OAuthException;
import com.interviewcoach.project.auth.exceptions.UserExistsException;
import com.interviewcoach.project.auth.refreshtoken.RefreshTokenService;
import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.enums.UserRole;
import com.interviewcoach.project.enums.UserStatus;

import com.interviewcoach.project.models.RefreshToken;
import com.interviewcoach.project.models.User;
import com.interviewcoach.project.security.JwtService;


import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

import java.util.UUID;

@Service
public class AuthService {
    private UserRepository userRepository;
    private PasswordEncoder myEncoder;
    private JwtService jwtService;
    private RefreshTokenService rtService;
    private ProfileManagementService pmService;

    public AuthService(UserRepository userRepository, PasswordEncoder myEncoder, JwtService jwtService,
            RefreshTokenService rtService, ProfileManagementService pmService) {
        this.userRepository = userRepository;
        this.myEncoder = myEncoder;
        this.jwtService = jwtService;
        this.rtService = rtService;
        this.pmService = pmService;
   
    }

    @Transactional
    public RegisteredDTO register(RegisterDTO dto) {

    
        Optional<User> myUser = userRepository.findByEmail(dto.email());

     
        if (myUser.isPresent()) {
            throw new UserExistsException("User already exists please login");

        }
    
        User user = dtoToModel(dto); 
        user = userRepository.save(user);
    
        pmService.createProfile(user);

        return getDto(user);

    }

    public LoggedinDTO login(LoginDTO dto) {
        User myUser = userRepository.findByEmail(dto.email()).orElseThrow();

        if (myUser.getAuthenticationSource().equals(AuthenticationSource.GOOGLE)) {
            throw new OAuthException("Login Using Google !!");
        }
        if (!validateCredentials(dto, myUser)) {
            throw new IncorrectCredentialsException("Incorrect credentials, Check email or Password. ");
        }

        rtService.validateSessions(myUser);
        String refreshToken = rtService.generateToken(myUser); 
        String jwtToken = jwtService.generateToken(myUser) ; 
        
        return new LoggedinDTO(jwtToken, refreshToken);

    }

    public User handleGoogleLogin(
            String email,
            String firstName,
            String lastName) {

        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;

        if (existingUser.isPresent()) {

            user = existingUser.get();

        } else {

            user = User.builder()
                    .userId(UUID.randomUUID())
                    .email(email)
                    .name(firstName + " " + lastName)
                    .userStatus(UserStatus.NEW)
                    .createdAt(LocalDateTime.now())
                    .authenticationSource(
                            AuthenticationSource.GOOGLE)
                    .userRole(UserRole.PENDING)
                    .build();
            pmService.createProfile(user) ; 
            user = userRepository.save(user);
        }

        rtService.validateSessions(user);

        return user;
    }

    public void logOut(String refreshToken) {
        if (refreshToken == null ||
                refreshToken.isBlank()) {

            throw new InvalidRefreshTokenException(
                    "Refresh token is invalid");
        }

        UUID refreshTokenId;

        try {
            refreshTokenId = UUID.fromString(refreshToken);
        } catch (IllegalArgumentException ex) {

            throw new RuntimeException(
                    "Malformed refresh token");
        }
        rtService.changeStatus(refreshTokenId);

    }


    public LoggedinDTO reIssueToken(
            String refreshToken) {

        if (refreshToken == null ||
                refreshToken.isBlank()) {

            throw new InvalidRefreshTokenException(
                    "Refresh token is invalid");
        }

        UUID refreshTokenId;

        try {
            refreshTokenId = UUID.fromString(refreshToken);
        } catch (IllegalArgumentException ex) {

            throw new RuntimeException(
                    "Malformed refresh token");
        }

        RefreshToken token = rtService.validate(refreshTokenId);

        String accessToken = jwtService.generateToken(
                token.getUser());

        return new LoggedinDTO(
                accessToken,
                refreshToken);
    }

    public String roleSetup(RoleSetupDTO dto) {
        String jwtToken = dto.jwtToken() ; 
        UserRole role = dto.role().equals(RoleDTO.CANDIDATE) ? UserRole.CANDIDATE : UserRole.INTERVIEWER ; 
        if (!jwtService.isTokenValid(jwtToken)) {
            throw new RuntimeException("INVALID JWT TOKEN, PLEASE LOGIN AGAIN"); 
        }
        String email = jwtService.getUsername(jwtToken) ; 
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IncorrectCredentialsException("PLEASE CHECK THE EMAIL AGAIN "));
        if (!user.getAuthenticationSource().equals(AuthenticationSource.GOOGLE)) {
            throw new RuntimeException("Cannot change the role for this user"); 
        }

        user.setUserRole(role);
        userRepository.save(user) ; 
        return "SUCCESS" ; 


    }

    private boolean validateCredentials(LoginDTO dto, User user) {

        if (myEncoder.matches(dto.password(), user.getEncodedPassword())) {
            return true;

        }
        return false;

    }

    private User dtoToModel(RegisterDTO dto) {

        User myUser = User.builder()
                .userId(UUID.randomUUID())
                .name(dto.name())
                .phoneNumber(dto.phoneNumber())
                .email(dto.email())
                .encodedPassword(myEncoder.encode(dto.password()))
                .userRole(dto.role().equals(RoleDTO.INTERVIEWER) ? UserRole.INTERVIEWER : UserRole.CANDIDATE)
                .userStatus(UserStatus.NEW)
                .createdAt(LocalDateTime.now())
                .authenticationSource(AuthenticationSource.LOCAL)
                .build();
        return myUser;
    }

    private RegisteredDTO getDto(User user) {
        return new RegisteredDTO(user.getEmail(), user.getUserStatus().equals(UserStatus.VERIFIED) ? true : false,
                true);
    }

}
