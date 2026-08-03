package com.interviewcoach.project;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/test/slow")
    public String hello() throws InterruptedException {
        
        return "Slow thread execution complete";
    }
}