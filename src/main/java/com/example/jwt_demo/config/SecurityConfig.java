package com.example.jwt_demo.config;

import com.example.jwt_demo.filter.JwtRequestFilter;
import com.example.jwt_demo.service.CustomUserDetailsService;
import com.example.jwt_demo.service.VendorDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final VendorDetailsService vendorDetailsService;

    public SecurityConfig(
            JwtRequestFilter jwtRequestFilter,
            CustomUserDetailsService customUserDetailsService,
            VendorDetailsService vendorDetailsService
    ) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.vendorDetailsService = vendorDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider userAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider vendorAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(vendorDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Primary
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(
                userAuthenticationProvider(),
                vendorAuthenticationProvider()
        ));
    }

    @Bean
    @Order(1)
    public SecurityFilterChain vendorSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/vendor/**")
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(vendorAuthenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/vendor/login", "/api/vendor/signup").permitAll()
                        .requestMatchers("/api/vendor/**").hasAuthority("VENDOR")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain userSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .securityMatcher("/api/user/**")
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(userAuthenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/login", "/api/user/signup").permitAll()
                        .requestMatchers("/api/user/**").hasAuthority("USER")
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    @Order(3)
    public SecurityFilterChain yogicAndSwaggerSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/v3/api-docs/**",
                                "/swagger-resources/**", "/webjars/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/yogic_attraction/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}