package com.interviewcoach.project.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.interviewcoach.project.auth.UserRepository;
import com.interviewcoach.project.auth.exceptions.UnverifiedException;
import com.interviewcoach.project.enums.UserStatus;
import com.interviewcoach.project.models.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

        private final JwtService jwtService;
        private final UserRepository userRepository;

        public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
                this.jwtService = jwtService;
                this.userRepository = userRepository;
        }

        @Override
        protected void doFilterInternal(
                        HttpServletRequest request,
                        HttpServletResponse response,
                        FilterChain filterChain)
                        throws ServletException, IOException {
                System.out.println("\n\n\n\n\nURI = " + request.getRequestURI()+"\n\n\n\n");
   
              
                if (request.getServletPath()
                                .equals("/auth/refresh")) {

                        filterChain.doFilter(
                                        request,
                                        response);

                        return;
                }
                String authHeader = request.getHeader("Authorization");

                if (authHeader == null ||
                                !authHeader.startsWith("Bearer ")) {

                        filterChain.doFilter(request, response);
                        return;
                }

                String jwt = authHeader.substring(7);

                if (!jwtService.isTokenValid(jwt)) {

                        filterChain.doFilter(request, response);
                        return;
                }

                if (request.getRequestURI().startsWith("/auth")) {
                        filterChain.doFilter(request, response);
                        return;
                }

                String email = jwtService.getUsername(jwt);

                String role = jwtService.getRole(jwt);

                User user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found"));

                if (user.getUserStatus() != UserStatus.VERIFIED) {
                        throw new UnverifiedException(
                                        "Please verify your account first");
                }

                List<GrantedAuthority> authorities = List.of(
                                new SimpleGrantedAuthority(
                                                "ROLE_" + role));

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                authorities);

                SecurityContextHolder
                                .getContext()
                                .setAuthentication(authToken);

                filterChain.doFilter(request, response);
        }
        
}


