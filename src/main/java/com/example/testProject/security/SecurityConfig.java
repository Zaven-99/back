package com.example.testProject.security;

import com.example.testProject.error.ApiError;
 import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final ObjectMapper objectMapper;

    public SecurityConfig(JwtFilter jwtFilter, ObjectMapper objectMapper) {
        this.jwtFilter = jwtFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/api/business/**").hasAuthority("BUSINESS")
                        .requestMatchers("/api/businesses/**").hasAuthority("BUSINESS")

                        .requestMatchers("/user/**").hasAuthority("USER")

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            ApiError body = new ApiError(
                                    Instant.now(),
                                    401,
                                    "Unauthorized",
                                    "Authentication required",
                                    request.getRequestURI()
                            );
                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            ApiError body = new ApiError(
                                    Instant.now(),
                                    403,
                                    "Forbidden",
                                    "Access denied",
                                    request.getRequestURI()
                            );
                            response.getWriter().write(objectMapper.writeValueAsString(body));
                        })
                )
                .addFilterBefore(jwtFilter,
                        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}