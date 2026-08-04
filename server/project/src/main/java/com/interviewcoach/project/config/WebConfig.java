package com.interviewcoach.project.config;

import com.interviewcoach.project.filters.RequestRouteInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final RequestRouteInterceptor requestRouteInterceptor;

    public WebConfig(RequestRouteInterceptor requestRouteInterceptor) {
        this.requestRouteInterceptor = requestRouteInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestRouteInterceptor);
    }
}