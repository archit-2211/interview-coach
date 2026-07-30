package com.interviewcoach.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthConfig {

    @Bean
    public PasswordEncoder myEncoder() {
        return new BCryptPasswordEncoder() ; 
    }
    
}
