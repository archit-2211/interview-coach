package com.interviewcoach.project.filters;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Order(1)
public class RequestIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        long startTime = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        MDC.put("requestId", requestId);
        MDC.put("method", request.getMethod());

        /*
         * MDC stands for Mapped Diagnostic Context
         * This is a simple built in and common mechanism to provide requestId
         * So whenever we log in the system, the MDC is automatically assigned to the
         * log
         * 
         * 
         */

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startTime;

            Object route = request.getAttribute(
                    HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

            if (route != null) {
                MDC.put("route", route.toString());
            }
            else {
                MDC.put("route", "INVALID ROUTE");
            }

            MDC.put("status", String.valueOf(response.getStatus()));
            MDC.put("durationMs", String.valueOf(durationMs));

            log.info("HTTP request completed");

            MDC.clear();

        }
    }

}
