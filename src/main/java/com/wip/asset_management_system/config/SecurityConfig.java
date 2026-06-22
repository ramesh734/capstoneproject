package com.wip.asset_management_system.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth

                // PUBLIC PAGES
                .requestMatchers(
                        "/",
                        "/index.html",
                        "/css/**",
                        "/js/**",
                        "/admin/**",
                        "/employee/**"
                ).permitAll()

                // ACTUATOR HEALTH
                .requestMatchers("/actuator/health")
                .permitAll()

                // LOGIN API
                .requestMatchers("/auth/**")
                .permitAll()

                // GET -> ADMIN + EMPLOYEE
                .requestMatchers(HttpMethod.GET, "/api/**")
                .hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE")

                // EMPLOYEE + ADMIN CAN CREATE MAINTENANCE
                .requestMatchers(HttpMethod.POST,
                        "/api/maintenance/**")
                .hasAnyAuthority(
                        "ROLE_ADMIN",
                        "ROLE_EMPLOYEE"
                )

                // ADMIN ONLY CREATE
                .requestMatchers(HttpMethod.POST,
                        "/api/**")
                .hasAuthority("ROLE_ADMIN")

                // ADMIN ONLY UPDATE
                .requestMatchers(HttpMethod.PUT,
                        "/api/**")
                .hasAuthority("ROLE_ADMIN")

                // ADMIN ONLY DELETE
                .requestMatchers(HttpMethod.DELETE,
                        "/api/**")
                .hasAuthority("ROLE_ADMIN")

                .anyRequest().authenticated()
            )

            .httpBasic(basic -> basic.authenticationEntryPoint(
                (request, response, authException) -> {
                    response.setContentType(
                            MediaType.APPLICATION_JSON_VALUE);
                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write(
                            "{\"error\":\"Unauthorized\"}");
                }
            ));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }
}