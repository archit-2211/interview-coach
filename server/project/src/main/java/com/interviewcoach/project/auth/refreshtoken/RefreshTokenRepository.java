package com.interviewcoach.project.auth.refreshtoken;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.interviewcoach.project.enums.RefreshTokenStatus;
import com.interviewcoach.project.models.RefreshToken;
import com.interviewcoach.project.models.User;

import java.util.List;





public interface RefreshTokenRepository extends JpaRepository<RefreshToken ,UUID> {
    List<RefreshToken> findByUserAndTokenStatusOrderByCreatedAtAsc(User user, RefreshTokenStatus tokenStatus) ; 
   
}
