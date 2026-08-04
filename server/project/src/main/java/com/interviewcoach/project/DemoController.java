package com.interviewcoach.project;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api")
public class DemoController {

    @GetMapping("/test/fast")
    public String hello()  {
        log.info("Loggin the test api");
        
        return "fast thread execution complete";
    }

    @GetMapping("/test/slow")
      public String hello2() throws InterruptedException {
        log.info("Loggin the test api");
        Thread.sleep(3000);
        
        return "Slow thread execution complete";
    }



}