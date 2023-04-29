package com.example.diplomproject.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.diplomproject.dto.CreateAds;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.UserRepository;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.security.core.Authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
class AdsControllerTest  {
    @Autowired
    MockMvc mockMvcAds;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;

    private Authentication authentication;
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    private final MockPart image = new MockPart("image", "image", "image".getBytes());
    private final User user = new User();

    private final Ads ads = new Ads();
    private final CreateAds createAds = new CreateAds();
    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("test@test.ru");
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setPhone("+79876543210");
        user.setPassword(encoder.encode("aqws123"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepository.save(user);

        createAds.setPrice(10);
        createAds.setTitle("title");
        createAds.setDescription("horror");

        ads.setPrice(20);
        ads.setDescription("comedy");
        ads.setTitle("title");
        ads.setAuthor(user);

        adsRepository.save(ads);

            }

    @AfterEach
    void tearDown() {
        adsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void getAllAds() throws Exception {
        mockMvcAds.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.result").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(username = "test2@test.ru", password = "123aqws", roles = "ADMIN")
    void testUpdateByAdmin() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"price\": \"15\",\n" +
                                "  \"title\": \"title9\",\n" +
                                " \"description\": \"aboutNew\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title9"));

    }
    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void updateImageByOtherUser() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getPk() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isForbidden());
    }

        @Test
    @WithMockUser(username = "test@test.ru", password = "aqws123")
    void testUpdateImage() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getPk() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "test2@test.ru", password = "123aqws", roles = "ADMIN")
    void testUpdateImageByAdmin() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getPk() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isOk());

    }
    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void updateAdsByAnotherUser() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getPk())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"price\": \"15\",\n" +
                                "  \"title\": \"title2\",\n" +
                                " \"description\": \"aboutNew\"}"))
                .andExpect(status().isForbidden());

    }
}