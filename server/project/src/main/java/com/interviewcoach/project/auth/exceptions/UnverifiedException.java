package com.interviewcoach.project.auth.exceptions;

public class UnverifiedException extends RuntimeException {
    public UnverifiedException() {
        super();
    }

    public UnverifiedException(String message) {
        super(message);
    }

    public UnverifiedException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
    
}
