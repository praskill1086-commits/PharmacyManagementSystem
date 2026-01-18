package com.ey.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.ey.repository.UserRepository;
import com.ey.security.CustomUserDetailsService;
import com.ey.security.JwtAuthenticationFilter;
import com.ey.security.JwtAuthorizationFilter;
import com.ey.security.JwtUtil;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {

        JwtAuthenticationFilter jwtAuthFilter =
                new JwtAuthenticationFilter(authManager, jwtUtil, userRepository);

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/forgot-password", "/auth/reset-password").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/customers/**").hasAnyRole("STAFF","ADMIN")
                        .requestMatchers("/sales/**").hasAnyRole("STAFF","ADMIN")
                        .requestMatchers("/dealers/**").hasAnyRole("ADMIN", "PHARMACIST")
                        .requestMatchers("/medicines/**").hasAnyRole("ADMIN", "PHARMACIST")
                        .requestMatchers("/offers/**").hasRole("ADMIN")
                        .requestMatchers("/wallets/**").hasAnyRole("ADMIN", "STAFF")
                        .requestMatchers("/expenses/**").hasRole("ADMIN")
                        .requestMatchers("/alerts/**").hasAnyRole("ADMIN", "PHARMACIST")
                        .requestMatchers("/refills/**").hasAnyRole("ADMIN", "PHARMACIST", "STAFF")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthorizationFilter(jwtUtil, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthFilter);


        return http.build();
    }
}

