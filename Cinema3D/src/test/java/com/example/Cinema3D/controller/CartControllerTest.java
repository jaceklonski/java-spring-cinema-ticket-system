package com.example.Cinema3D.controller;

import com.example.Cinema3D.entity.SeatStatus;
import com.example.Cinema3D.entity.TicketType;
import com.example.Cinema3D.entity.User;
import com.example.Cinema3D.repository.ScreeningSeatRepository;
import com.example.Cinema3D.repository.UserRepository;
import com.example.Cinema3D.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
@Import(CartControllerTest.TestSecurityConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreeningSeatRepository screeningSeatRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private CartService cartService;

    @Test
    void shouldShowCart() throws Exception {
        User user = new User();
        user.setUsername("john");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        when(screeningSeatRepository.findByReservedByAndStatus(
                user, SeatStatus.RESERVED))
                .thenReturn(List.of());

        mockMvc.perform(get("/cart")
                        .with(user("john"))) // <-- ustawia Principal / Authentication
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attributeExists("items"))
                .andExpect(model().attribute("ticketTypes", TicketType.values()));
    }

    @Test
    void shouldRemoveSeatFromCart() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/remove")
                        .session(session)
                        .with(csrf())
                        .with(user("john")) // jeśli kontroler oczekuje principal, warto dodać user też tutaj
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("screeningId", "1")
                        .param("row", "2")
                        .param("seat", "5")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService)
                .removeSeat(session, 1L, 2, 5);
    }

    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(auth -> auth
                            .anyRequest().permitAll()
                    );
            return http.build();
        }
    }
}
