package com.interviewcoach.project.auth;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.interviewcoach.project.models.User;

import java.util.Optional;



public interface UserRepository extends JpaRepository<User, UUID> {
Optional<User> findByEmail(String email);
}
