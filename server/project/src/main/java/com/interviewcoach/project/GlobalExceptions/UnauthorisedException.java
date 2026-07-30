package com.interviewcoach.project.GlobalExceptions;

public class UnauthorisedException extends RuntimeException {
    public UnauthorisedException() {
        super();
    }

    public UnauthorisedException(String message) {
        super(message);
    }

    public UnauthorisedException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
    
}
