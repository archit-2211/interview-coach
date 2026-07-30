package com.interviewcoach.project.ProfileManagement.exceptions;

public class InvalidExperienceException extends RuntimeException{

    public InvalidExperienceException () {
        super();
    }

    public InvalidExperienceException (String message) {
        super(message);
    }

    public InvalidExperienceException (
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }

    
    
}
