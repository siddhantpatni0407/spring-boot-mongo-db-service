package com.sid.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sid.app.constant.AppConstants;
import com.sid.app.entity.User;
import com.sid.app.exception.ResourceNotFoundException;
import com.sid.app.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;
    private User sampleUser;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Support Instant/LocalDateTime

        sampleUser = User.builder()
                .id("123")
                .name("John Doe")
                .email("john@example.com")
                .role("USER")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    void testAllUsers_Success() throws Exception {
        when(userService.findAll()).thenReturn(List.of(sampleUser));

        mockMvc.perform(get(AppConstants.USERS_API))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(AppConstants.STATUS_SUCCESS))
                .andExpect(jsonPath("$.data[0].email").value("john@example.com"));
    }

    @Test
    void testGetById_Success() throws Exception {
        when(userService.findById("123")).thenReturn(sampleUser);

        mockMvc.perform(get(AppConstants.USERS_API + "/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value("123"))
                .andExpect(jsonPath("$.data.name").value("John Doe"));
    }

    @Test
    void testGetById_NotFound() throws Exception {
        when(userService.findById("404")).thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(get(AppConstants.USERS_API + "/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.create(any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(post(AppConstants.USERS_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(AppConstants.STATUS_SUCCESS))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void testCreateUser_InvalidInput() throws Exception {
        User invalidUser = User.builder().id("321").name("").email("invalid").build();

        mockMvc.perform(post(AppConstants.USERS_API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());
    }


    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.update(eq("123"), any(User.class))).thenReturn(sampleUser);

        mockMvc.perform(put(AppConstants.USERS_API + "/123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppConstants.MSG_USER_UPDATED));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userService.update(eq("404"), any(User.class)))
                .thenThrow(new ResourceNotFoundException("User not found"));

        mockMvc.perform(put(AppConstants.USERS_API + "/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleUser)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).delete("123");

        mockMvc.perform(delete(AppConstants.USERS_API + "/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(AppConstants.MSG_USER_DELETED));
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        Mockito.doThrow(new ResourceNotFoundException("User not found")).when(userService).delete("404");

        mockMvc.perform(delete(AppConstants.USERS_API + "/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
