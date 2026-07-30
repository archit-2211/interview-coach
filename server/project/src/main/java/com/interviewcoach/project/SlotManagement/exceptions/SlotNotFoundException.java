package com.interviewcoach.project.SlotManagement.exceptions;

public class SlotNotFoundException extends RuntimeException {
    public SlotNotFoundException () {

    }
    public SlotNotFoundException (String message) {
        super(message) ; 
    } 
    
}
