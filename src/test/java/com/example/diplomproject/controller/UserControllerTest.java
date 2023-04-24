package com.example.diplomproject.controller;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.Avatar;
import com.example.diplomproject.model.Image;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AvatarRepository;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.checkerframework.checker.units.qual.A;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import javax.transaction.Transactional;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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
    private AvatarRepository avatarRepository;
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
    private final Avatar avatar = new Avatar();

    @BeforeEach
    void setUp() {
        user.setRole(Role.USER);
        user.setFirstName("test FirstName");
        user.setLastName("test LastName");
        user.setPhone("+7123456789");
        user.setUsername("test@test.test");
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
        avatarRepository.delete(avatar);
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

        user.setAvatar(null);
        avatarRepository.deleteAllById(Collections.singleton(avatar.getId()));
    }
}