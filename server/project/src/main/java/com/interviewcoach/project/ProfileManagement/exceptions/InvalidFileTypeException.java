package com.interviewcoach.project.ProfileManagement.exceptions;

public class InvalidFileTypeException extends RuntimeException{

    public InvalidFileTypeException() {
        super();
    }

    public InvalidFileTypeException(String message) {
        super(message);
    }

    public InvalidFileTypeException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
    
}
