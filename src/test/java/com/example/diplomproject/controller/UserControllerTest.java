package com.example.diplomproject.controller;
import com.example.diplomproject.dto.UserDTO;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.exception.UserNotFoundException;
import com.example.diplomproject.mapper.UserMapper;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import javax.transaction.Transactional;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomUserDetailsService manager;

    private Authentication authentication;
    private final User user = new User();
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());

    @BeforeEach
    void setUp() {
        user.setRole(Role.USER);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPhone("+79876543211");
        user.setUsername("test@test.ru");
        user.setPassword(encoder.encode("test1234"));
        user.setEnabled(true);
        userRepository.save(user);

        UserDetails userDetails = manager
                .loadUserByUsername(user.getUsername());

        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());
    }

    @AfterEach
    void deleteAll() {
        userRepository.delete(user);
    }

    @Test
    public void testUpdatePassword() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("currentPassword", "test1234");
        jsonObject.put("newPassword", "1234test");

        mockMvc.perform(post("/users/set_password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString())
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetUser() throws Exception {
        mockMvc.perform(get("/users/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value(user.getUsername()));
    }

    @Test
    public void testUpdateUser() throws Exception {
        String newFirstName = "Test1";
        String newLastName = "Test2";
        user.setFirstName(newFirstName);
        user.setLastName(newLastName);

        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName")
                        .value(newFirstName))
                .andExpect(jsonPath("$.lastName")
                        .value(newLastName));
    }
    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void updateUser() throws Exception {
        UserDTO userDTO = UserMapper.toDTO(userRepository.findByUsernameIgnoreCase(user.getUsername()).orElseThrow(UserNotFoundException::new));
        userDTO.setEmail("2@mail.ru");
        mockMvc.perform(patch("/users/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDTO.getId()))
                .andExpect(jsonPath("$.email").value("2@mail.ru"));
    }
    @Test
    @Transactional
    public void testUpdateUserAvatar() throws Exception {
        mockMvc.perform(patch("/users/me/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(request -> {
                            request.addPart(imageFile);
                            return request;
                        })
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @WithMockUser(username = "test@test.ru", password = "aqws123")
    void updateUserImage() throws Exception {
        MockPart image = new MockPart("image", "avatar", "userAvatar".getBytes());
        mockMvc.perform(patch("/users/me/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isOk());
    }


}