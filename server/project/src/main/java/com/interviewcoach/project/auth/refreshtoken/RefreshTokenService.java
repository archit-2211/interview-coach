package com.interviewcoach.project.auth.refreshtoken;

import java.time.Instant;
import java.util.List;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.interviewcoach.project.enums.RefreshTokenStatus;
import com.interviewcoach.project.models.RefreshToken;
import com.interviewcoach.project.models.User;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
    private RefreshTokenRepository myRepository;
    private static final int MAX_ACTIVE_SESSIONS = 3;

    public RefreshTokenService(RefreshTokenRepository myRepository) {
        this.myRepository = myRepository;
    }

    public String generateToken(User user) {
        UUID tokenValue = UUID.randomUUID();
        RefreshToken myToken = RefreshToken.builder().tokenValue(tokenValue).user(user).build();

        myRepository.save(myToken);
        return tokenValue.toString();

    }

    private List<RefreshToken> getActiveTokens(User user) {
        List<RefreshToken> activeTokens = myRepository.findByUserAndTokenStatusOrderByCreatedAtAsc(user, RefreshTokenStatus.Active);
        return activeTokens;

    }

    @Transactional
    public void validateSessions(User user) {
        List<RefreshToken> activeTokens = getActiveTokens(user);
        if (activeTokens.size() >= MAX_ACTIVE_SESSIONS) {
            RefreshToken oldestToken = activeTokens.get(0);
            oldestToken.setTokenStatus(RefreshTokenStatus.Inactive);
            myRepository.save(oldestToken);

        }

    }

    public RefreshToken validate(UUID tokenValue) {

        RefreshToken token = myRepository
                .findById(tokenValue)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Refresh Token Not Found"));

        if (token.getTokenStatus() != RefreshTokenStatus.Active) {

            throw new RuntimeException(
                    "Refresh Token Revoked");
        }

        if (token.getExpiryAt()
                .isBefore(Instant.now())) {

            throw new RuntimeException(
                    "Refresh Token Expired");
        }

        return token;
    }

    @Transactional
    public void changeStatus(UUID tokenValue) {
        RefreshToken token = myRepository.findById(tokenValue).orElseThrow();
        token.setTokenStatus(RefreshTokenStatus.Inactive);
        myRepository.save(token) ; 
    }

}
