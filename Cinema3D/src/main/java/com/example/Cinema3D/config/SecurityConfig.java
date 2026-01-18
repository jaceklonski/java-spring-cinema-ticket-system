package com.example.Cinema3D.config;

import com.example.Cinema3D.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    // ===============================
    // PASSWORD ENCODER
    // ===============================
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ===============================
    // AUTHENTICATION PROVIDER
    // ===============================
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ===============================
    // SECURITY FILTER CHAIN
    // ===============================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                // CSRF wyłączone (fetch + REST)
                .csrf(csrf -> csrf.disable())

                // AUTORYZACJA
                .authorizeHttpRequests(auth -> auth

                        // 🔓 WYJĄTEK: kliknięcie miejsca = rezerwacja
                        .requestMatchers("/api/v1/seats/reserve").permitAll()

                        // PUBLIC
                        .requestMatchers(
                                "/",
                                "/cinema/**",
                                "/login",
                                "/register",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/css/**",
                                "/js/**"
                        ).permitAll()

                        // ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // USER (RESZTA API)
                        .requestMatchers("/api/v1/**").hasRole("USER")

                        // RESZTA
                        .anyRequest().authenticated()
                )

                // FORM LOGIN
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/cinema", true)
                        .permitAll()
                )

                // LOGOUT
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }
}
