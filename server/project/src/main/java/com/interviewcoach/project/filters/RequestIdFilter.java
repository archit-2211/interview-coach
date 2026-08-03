package com.interviewcoach.project.filters;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String requestId = UUID.randomUUID().toString() ; 
                MDC.put("requestId", requestId);

                /*
                MDC stands for Mapped Diagnostic Context 
                This is a simple built in and common mechanism to provide requestId 
                So whenever we log in the system, the MDC is automatically assigned to the log 
                
                
                */


                try {
                    filterChain.doFilter(request, response);
                }
                finally {
                    MDC.remove("requestId");

                }
    }
    
}
