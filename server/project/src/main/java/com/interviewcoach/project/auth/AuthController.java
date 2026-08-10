package com.interviewcoach.project.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.interviewcoach.project.auth.dto.LoggedinDTO;
import com.interviewcoach.project.auth.dto.LoggedinResponse;
import com.interviewcoach.project.auth.dto.LoginDTO;
import com.interviewcoach.project.auth.dto.RegisterDTO;
import com.interviewcoach.project.auth.dto.RegisteredDTO;
import com.interviewcoach.project.auth.dto.RoleSetupDTO;
import com.interviewcoach.project.auth.exceptions.InvalidRefreshTokenException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

@RequestMapping("/auth")
@RestController
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;

    }

    @PostMapping("/register")
    public ResponseEntity<RegisteredDTO> register(@RequestBody @Valid RegisterDTO mydto) {
        RegisteredDTO response = authService.register(mydto);

        return new ResponseEntity<RegisteredDTO>(response, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<LoggedinResponse> login(@RequestBody LoginDTO myDto, HttpServletResponse responseHttp) {
        LoggedinDTO response = authService.login(myDto);
        ResponseCookie myCookie = ResponseCookie.from("refreshToken", response.refreshToken()).httpOnly(true)
                .maxAge(50 * 60 * 24 * 30).sameSite("Lax").secure(true).path("/").build();
        responseHttp.addHeader(HttpHeaders.SET_COOKIE, myCookie.toString());

        return new ResponseEntity<>(new LoggedinResponse(response.bearerToken()), HttpStatus.OK);

    }

    @PostMapping("/refresh")
    public ResponseEntity<LoggedinResponse> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken) {
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException("Check your token ! Nothing found");
        }
        LoggedinDTO response = authService.reIssueToken(refreshToken);
        return new ResponseEntity<>(new LoggedinResponse(response.bearerToken()), HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {

   
        if (refreshToken == null) {
            throw new InvalidRefreshTokenException("Check your token ! Nothing found");
        }

        authService.logOut(refreshToken);
        ResponseCookie deleteCookie = ResponseCookie.from(
                "refreshToken",
                "")
                .httpOnly(true)
                .maxAge(0)
                .sameSite("Lax")
                .secure(true)
                .path("/")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie.toString());
        return ResponseEntity.ok("Log out has been successful");
    }

    @PostMapping("/role/setup")
    public ResponseEntity<String> roleSetup(@RequestBody RoleSetupDTO dto) {
        String response = authService.roleSetup(dto);
        return ResponseEntity.ok(response);

    }

   

}
