package com.interviewcoach.project.auth.exceptions;

public class InvalidRefreshTokenException
        extends RuntimeException {

    public InvalidRefreshTokenException() {
    }

    public InvalidRefreshTokenException(
            String message
    ) {
        super(message);
    }
}