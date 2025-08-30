package com.sid.app.service;

import com.sid.app.entity.User;
import com.sid.app.exception.ResourceNotFoundException;
import com.sid.app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userService = new UserService(userRepository);

        sampleUser = User.builder()
                .id("123")
                .name("John Doe")
                .email("john@example.com")
                .role("USER")
                .build();
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(List.of(sampleUser));

        List<User> result = userService.findAll();

        assertEquals(1, result.size());
        assertEquals("john@example.com", result.get(0).getEmail());
    }

    @Test
    void testFindById_Found() {
        when(userRepository.findById("123")).thenReturn(Optional.of(sampleUser));

        User result = userService.findById("123");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void testFindById_NotFound() {
        when(userRepository.findById("999")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> userService.findById("999"));
    }

    @Test
    void testCreate() {
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User result = userService.create(sampleUser);

        assertNotNull(result);
        assertEquals("123", result.getId());
        verify(userRepository, times(1)).save(sampleUser);
    }

    @Test
    void testUpdate() {
        when(userRepository.findById("123")).thenReturn(Optional.of(sampleUser));
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        User updated = User.builder()
                .id("123")
                .name("Jane Doe")
                .email("jane@example.com")
                .role("ADMIN")
                .build();

        User result = userService.update("123", updated);

        assertEquals("Jane Doe", result.getName());
        assertEquals("ADMIN", result.getRole());
    }

    @Test
    void testDelete() {
        when(userRepository.findById("123")).thenReturn(Optional.of(sampleUser));
        doNothing().when(userRepository).delete(sampleUser);

        userService.delete("123");

        verify(userRepository, times(1)).delete(sampleUser);
    }
}
