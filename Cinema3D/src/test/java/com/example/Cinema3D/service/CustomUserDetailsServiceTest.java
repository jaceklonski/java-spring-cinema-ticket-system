package com.example.Cinema3D.service;

import com.example.Cinema3D.entity.User;
import com.example.Cinema3D.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.*;

import java.util.Optional;

import com.example.Cinema3D.entity.Role;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsername_returnsUserDetailsWhenUserExists() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("secret");
        user.setRole(Role.ADMIN);

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        UserDetails userDetails =
                userDetailsService.loadUserByUsername("john");

        assertEquals("john", userDetails.getUsername());
        assertEquals("secret", userDetails.getPassword());

        assertEquals(1, userDetails.getAuthorities().size());
        GrantedAuthority authority =
                userDetails.getAuthorities().iterator().next();

        assertEquals("ROLE_ADMIN", authority.getAuthority());
    }

    @Test
    void loadUserByUsername_throwsExceptionWhenUserNotFound() {
        when(userRepository.findByUsername("unknown"))
                .thenReturn(Optional.empty());

        assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("unknown")
        );
    }
}
