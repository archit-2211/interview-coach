package com.interviewcoach.project.SlotManagement.exceptions;

public class InvalidSlotException extends RuntimeException {
    public InvalidSlotException () {

    }
    public InvalidSlotException(String message) {
        super(message) ; 
    } 
    
}
