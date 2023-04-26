package com.example.diplomproject.controller;

import com.example.diplomproject.dto.LoginReq;
import com.example.diplomproject.dto.RegisterReq;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.User;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
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
    private UserService userService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;
    private LoginReq loginReq;
    @BeforeEach
    void setUp() {
    User user = new User();
        user.setUsername("test@test.ru");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPhone("+79876543211");
        user.setPassword(encoder.encode("1234qwer"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);
        loginReq = new LoginReq();
        loginReq.setPassword("1234qwer");
        loginReq.setUsername(user.getUsername());}

        @AfterEach
        void tearDown() {
            userRepository.deleteAll();
        }
    @Test
    public void testRegister() throws Exception {
        RegisterReq registerReq = new RegisterReq();
        registerReq.setUsername("test@test.test");
        registerReq.setPassword("testPassword");
        registerReq.setFirstName("User1");
        registerReq.setLastName("Test1");
        registerReq.setPhone("+79876543211");
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
//    @Test
//    @WithAnonymousUser
//    void login() throws Exception {
//        mockMvc.perform(post("/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(loginReq))
//                .accept(String.valueOf(status().isOk())).}

}