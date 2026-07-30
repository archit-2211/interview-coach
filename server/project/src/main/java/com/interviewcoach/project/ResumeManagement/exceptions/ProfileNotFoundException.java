package com.interviewcoach.project.ResumeManagement.exceptions;

public class ProfileNotFoundException extends RuntimeException {
    public ProfileNotFoundException() {
        super();
    }

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
    
}

                   
