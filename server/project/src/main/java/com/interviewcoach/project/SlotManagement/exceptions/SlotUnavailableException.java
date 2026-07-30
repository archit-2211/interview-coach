package com.interviewcoach.project.SlotManagement.exceptions;

public class SlotUnavailableException extends RuntimeException{

    public SlotUnavailableException () {

    }
    public SlotUnavailableException (String message) {
        super(message) ; 
    } 
    
}
