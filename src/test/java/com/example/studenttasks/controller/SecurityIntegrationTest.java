package com.example.studenttasks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void accessProtectedEndpoint_WithoutAuth_Expect403() throws Exception {
        // Trying to get tasks without logging in
        mockMvc.perform(get("/tasks/my"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER") // Simulates a logged-in user with ROLE_USER
    void accessAdminEndpoint_WithUserRole_Expect403() throws Exception {
        // A regular user trying to delete a task (Admin only)
        mockMvc.perform(delete("/tasks/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    void registerUser_Expect201Created() throws Exception {
        // A clean JSON payload for registration
        String userJson = """
                {
                    "username": "newuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());
    }
}