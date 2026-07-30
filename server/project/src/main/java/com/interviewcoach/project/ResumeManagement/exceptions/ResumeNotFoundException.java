package com.interviewcoach.project.ResumeManagement.exceptions;

public class ResumeNotFoundException extends RuntimeException {
    public ResumeNotFoundException() {
        super();
    }

    public ResumeNotFoundException(String message) {
        super(message);
    }

    public ResumeNotFoundException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }


    
}
