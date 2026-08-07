package com.interviewcoach.project.auth;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

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

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder myEncoder;
    @Mock
    JwtService jwtService;
    @Mock
    RefreshTokenService rtService;
    @Mock
    ProfileManagementService pmService;
    @InjectMocks
    AuthService authService;

    @Test
    void register_ShouldSaveUser() {

        RegisterDTO dto = new RegisterDTO(
                "Archit",
                "9989543212",
                "archit@gmail.com",
                "password123",
                RoleDTO.CANDIDATE);

        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.empty());

        User savedUser = new User();
        savedUser.setEmail(dto.email());

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        RegisteredDTO result = authService.register(dto);

        assertEquals("archit@gmail.com", result.email());

        verify(userRepository).findByEmail(dto.email());
        verify(userRepository).save(any(User.class));
        verify(pmService).createProfile(savedUser);
    }

    @Test
    void register_ShouldNotSaveUserWhenEmailExists() {
        RegisterDTO dto = new RegisterDTO(
                "Archit",
                "9989543212",
                "archit@gmail.com",
                "password123",
                RoleDTO.CANDIDATE);
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));
        User user = new User();
        user.setEmail(dto.email());

        assertThrows(UserExistsException.class, () -> authService.register(dto));

        verify(userRepository).findByEmail(dto.email());

        verify(userRepository, never()).save(any(User.class));
        verifyNoInteractions(pmService);

    }

    @Test
    void login_SuccessfulLogin() {
        LoginDTO dto = new LoginDTO("test@example.com", "testtest");
        User user = new User();
        user.setEncodedPassword("something");
        user.setEmail(dto.email());
        user.setAuthenticationSource(AuthenticationSource.LOCAL);
        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(user));
        when(myEncoder.matches(dto.password(), user.getEncodedPassword())).thenReturn(true);
        when(rtService.generateToken(user)).thenReturn("DemoRefreshToken");
        when(jwtService.generateToken(user)).thenReturn("demo JWT Token");
        LoggedinDTO ldto = new LoggedinDTO("demo JWT Token", "DemoRefreshToken");
        LoggedinDTO result = authService.login(dto);

        assertEquals(ldto, result);
        verify(rtService).validateSessions(user);
        verify(userRepository).findByEmail(dto.email());
        verify(myEncoder).matches(dto.password(), user.getEncodedPassword());
        verify(rtService).generateToken(user);
        verify(jwtService).generateToken(user);

    }

    @Test
    void login_failedLoginDueToGoogleAuthentication() {
        LoginDTO dto = new LoginDTO("test@example.com", "testtest");
        User user = new User();
        user.setEncodedPassword("something");
        user.setEmail(dto.email());
        user.setAuthenticationSource(AuthenticationSource.GOOGLE);
        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(user));
        assertThrows(OAuthException.class, () -> authService.login(dto));
        verify(userRepository).findByEmail(dto.email());
        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);

    }

    @Test
    void login_failedLoginDueToIncorrectCredentials() {
        LoginDTO dto = new LoginDTO("test@example.com", "testtest");
        User user = new User();
        user.setEncodedPassword("something");
        user.setEmail(dto.email());
        user.setAuthenticationSource(AuthenticationSource.LOCAL);
        when(userRepository.findByEmail(dto.email()))
                .thenReturn(Optional.of(user));
        when(myEncoder.matches(dto.password(), user.getEncodedPassword())).thenReturn(false);

        assertThrows(IncorrectCredentialsException.class, () -> authService.login(dto));
        verify(userRepository).findByEmail(dto.email());
        verify(myEncoder).matches(dto.password(), user.getEncodedPassword());

        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);

    }

    @Test
    void login_failedLoginDueToEmail() {
        LoginDTO dto = new LoginDTO("test@example.com", "testtest");
        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> authService.login(dto));
        verify(userRepository).findByEmail(dto.email());
        verifyNoInteractions(myEncoder);
        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);

    }

    @Test
    void handleGoogleLogin_ExistingUser() {

        String email = "test@example.com";

        User existingUser = new User();
        existingUser.setEmail(email);
        existingUser.setAuthenticationSource(AuthenticationSource.GOOGLE);

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(existingUser));

        User result = authService.handleGoogleLogin(
                email,
                "Archit",
                "Agarwal");

        assertEquals(existingUser, result);

        verify(userRepository).findByEmail(email);
        verify(userRepository, never()).save(any(User.class));
        verify(rtService).validateSessions(existingUser);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void handleGoogleLogin_NewUser() {

        String email = "test@example.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setUserId(UUID.randomUUID());
                    return user;
                });

        User result = authService.handleGoogleLogin(
                email,
                "Archit",
                "Agarwal");

        assertEquals(email, result.getEmail());
        assertEquals("Archit Agarwal", result.getName());
        assertEquals(AuthenticationSource.GOOGLE, result.getAuthenticationSource());
        assertEquals(UserRole.PENDING, result.getUserRole());
        assertEquals(UserStatus.NEW, result.getUserStatus());

        verify(userRepository).findByEmail(email);
        verify(userRepository).save(any(User.class));
        verify(rtService).validateSessions(result);

        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void logOut_SuccessfulLogout() {

        String refreshToken = UUID.randomUUID().toString();

        assertDoesNotThrow(() -> authService.logOut(refreshToken));

        verify(rtService).changeStatus(UUID.fromString(refreshToken));
    }

    @Test
    void logOut_NullRefreshToken() {

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.logOut(null));

        verifyNoInteractions(rtService);
    }

    @Test
    void logOut_BlankRefreshToken() {

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.logOut("   "));

        verifyNoInteractions(rtService);
    }

    @Test
    void logOut_InvalidRefreshTokenFormat() {

        String refreshToken = "invalid-token";

        assertThrows(RuntimeException.class,
                () -> authService.logOut(refreshToken));

        verifyNoInteractions(rtService);
    }

    @Test
    void reIssueToken_Success() {

        String refreshToken = UUID.randomUUID().toString();

        User user = new User();

        RefreshToken token = new RefreshToken();
        token.setUser(user);

        when(rtService.validate(UUID.fromString(refreshToken)))
                .thenReturn(token);

        when(jwtService.generateToken(user))
                .thenReturn("jwt-token");

        LoggedinDTO result = authService.reIssueToken(refreshToken);

        assertEquals(new LoggedinDTO("jwt-token", refreshToken), result);

        verify(rtService).validate(UUID.fromString(refreshToken));
        verify(jwtService).generateToken(user);
    }

    @Test
    void reIssueToken_NullRefreshToken() {

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.reIssueToken(null));

        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);
    }

    @Test
    void reIssueToken_BlankRefreshToken() {

        assertThrows(InvalidRefreshTokenException.class,
                () -> authService.reIssueToken("   "));

        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);
    }

    @Test
    void reIssueToken_InvalidUUID() {

        assertThrows(RuntimeException.class,
                () -> authService.reIssueToken("invalid-token"));

        verifyNoInteractions(rtService);
        verifyNoInteractions(jwtService);
    }

    @Test
    void roleSetup_Success() {

        String jwt = "jwt-token";

        RoleSetupDTO dto = new RoleSetupDTO(RoleDTO.CANDIDATE, jwt);

        User user = new User();
        user.setAuthenticationSource(AuthenticationSource.GOOGLE);

        when(jwtService.isTokenValid(jwt)).thenReturn(true);
        when(jwtService.getUsername(jwt)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        String result = authService.roleSetup(dto);

        assertEquals("SUCCESS", result);
        assertEquals(UserRole.CANDIDATE, user.getUserRole());

        verify(jwtService).isTokenValid(jwt);
        verify(jwtService).getUsername(jwt);
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).save(user);
    }

    @Test
    void roleSetup_InvalidJwt() {

        String jwt = "jwt-token";

        RoleSetupDTO dto = new RoleSetupDTO(RoleDTO.CANDIDATE, jwt);

        when(jwtService.isTokenValid(jwt)).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.roleSetup(dto));

        verify(jwtService).isTokenValid(jwt);

        verifyNoInteractions(userRepository);
    }

    @Test
    void roleSetup_UserNotFound() {

        String jwt = "jwt-token";

        RoleSetupDTO dto = new RoleSetupDTO(RoleDTO.CANDIDATE, jwt);

        when(jwtService.isTokenValid(jwt)).thenReturn(true);
        when(jwtService.getUsername(jwt)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(IncorrectCredentialsException.class,
                () -> authService.roleSetup(dto));

        verify(jwtService).isTokenValid(jwt);
        verify(jwtService).getUsername(jwt);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void roleSetup_LocalUserCannotChangeRole() {

        String jwt = "jwt-token";

        RoleSetupDTO dto = new RoleSetupDTO(RoleDTO.CANDIDATE, jwt);

        User user = new User();
        user.setAuthenticationSource(AuthenticationSource.LOCAL);

        when(jwtService.isTokenValid(jwt)).thenReturn(true);
        when(jwtService.getUsername(jwt)).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> authService.roleSetup(dto));

        verify(userRepository, never()).save(any(User.class));
    }

}
