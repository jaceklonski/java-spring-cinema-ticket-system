package com.example.Cinema3D.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // REST API – wyłączamy CSRF
            .csrf(csrf -> csrf.disable())

            // Na razie pozwalamy na wszystko
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )

            // Wyłączamy formularz logowania
            .formLogin(form -> form.disable())

            // Wyłączamy basic auth
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
