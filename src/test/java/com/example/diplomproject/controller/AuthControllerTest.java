package com.example.diplomproject.controller;

import com.example.diplomproject.dto.RegisterReq;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegister() throws Exception {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setUsername("test@test.test");
        registerReq.setPassword("testPassword");
        registerReq.setFirstName("User2");
        registerReq.setLastName("Test2");
        registerReq.setPhone("+79609279285");
        registerReq.setPassword("password2");
        registerReq.setRole(Role.ADMIN);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isOk());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", "test@test.test");
        jsonObject.put("password", "password2");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk());

        userService.deleteUser(registerReq.getUsername());
    }

}