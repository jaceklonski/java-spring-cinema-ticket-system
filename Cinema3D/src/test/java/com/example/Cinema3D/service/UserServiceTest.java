package com.example.Cinema3D.service;

import com.example.Cinema3D.dto.RegisterRequest;
import com.example.Cinema3D.entity.Role;
import com.example.Cinema3D.entity.User;
import com.example.Cinema3D.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    UserService userService;

    @Test
    void register_createsRegularUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("password");
        request.setConfirmPassword("password");
        request.setAdmin(false);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password"))
                .thenReturn("hashed");

        userService.register(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        User saved = captor.getValue();
        assertEquals("john", saved.getUsername());
        assertEquals("hashed", saved.getPassword());
        assertEquals(Role.USER, saved.getRole());
    }

    @Test
    void register_createsAdminUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("admin");
        request.setPassword("password");
        request.setConfirmPassword("password");
        request.setAdmin(true);

        when(userRepository.findByUsername("admin"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("password"))
                .thenReturn("hashed");

        userService.register(request);

        ArgumentCaptor<User> captor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(captor.capture());

        assertEquals(Role.ADMIN, captor.getValue().getRole());
    }

    @Test
    void register_throwsWhenPasswordsDoNotMatch() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("pass1");
        request.setConfirmPassword("pass2");

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verifyNoInteractions(userRepository);
        verifyNoInteractions(passwordEncoder);
    }

    @Test
    void register_throwsWhenUsernameAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setPassword("password");
        request.setConfirmPassword("password");

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(new User()));

        assertThrows(
                IllegalArgumentException.class,
                () -> userService.register(request)
        );

        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}
