package com.interviewcoach.project;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.tracing.Tracer;

import io.micrometer.tracing.Span;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/api")
public class DemoController {
    private final Tracer tracer;

    public DemoController(Tracer tracer) {
        this.tracer = tracer;
    }

    @GetMapping("/test/fast")
    public String hello() {
        log.info("Loggin the test api");

        return "fast thread execution complete";
    }

    @GetMapping("/test/slow")
    public String hello2() throws InterruptedException {
        log.info("Loggin the test api");
        Thread.sleep(3000);

        return "Slow thread execution complete";
    }

    @GetMapping("/test/trace")
    public String traceTest() throws InterruptedException {

        Span span = tracer.nextSpan().name("simulate-slow-operation").start();

        try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {

            Thread.sleep(500);

            return "Trace test completed";

        } finally {
            span.end();
        }
    }


    @GetMapping("/test/error")
    public String errorMethod() throws InterruptedException {
        traceTest() ; 
        throw new RuntimeException("Testing distributed tracing error");


        
    }
    

}