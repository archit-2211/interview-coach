package com.interviewcoach.project.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import com.interviewcoach.project.enums.AuthenticationSource;
import com.interviewcoach.project.models.User;


@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository ; 


    @Test
    void findByEmail_success() {
        User user = new User();
        UUID userId = UUID.randomUUID(); 
        user.setUserId(userId);
        user.setEmail("something@example.com");
        user.setAuthenticationSource(AuthenticationSource.LOCAL);
        
        userRepository.save(user) ; 

        Optional<User> opUser = userRepository.findByEmail("something@example.com") ; 

       assertTrue(opUser.isPresent()) ; 
       assertEquals(userId, opUser.get().getUserId());
       assertEquals(AuthenticationSource.LOCAL, opUser.get().getAuthenticationSource());


    }
    @Test
    void findByEmail_fail() {

        Optional<User> opUser = userRepository.findByEmail("something@example.com") ; 

        assertTrue(opUser.isEmpty()) ; 

    }
}
