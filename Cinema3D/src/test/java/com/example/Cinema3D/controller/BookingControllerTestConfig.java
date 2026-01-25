package com.example.Cinema3D.controller;

import com.example.Cinema3D.repository.BookingRepository;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class BookingControllerTestConfig {

    @Bean
    @Primary
    BookingRepository bookingRepository() {
        return Mockito.mock(BookingRepository.class);
    }

    @Bean
    @Primary
    ScreeningSeatRepository screeningSeatRepository() {
        return Mockito.mock(ScreeningSeatRepository.class);
    }

    @Bean
    @Primary
    UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}
