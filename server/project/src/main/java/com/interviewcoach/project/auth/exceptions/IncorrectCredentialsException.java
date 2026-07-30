package com.interviewcoach.project.auth.exceptions;

public class IncorrectCredentialsException extends RuntimeException {
    public IncorrectCredentialsException() {

    }
    public IncorrectCredentialsException(String message) {
        super(message) ; 
    } 
    
}
