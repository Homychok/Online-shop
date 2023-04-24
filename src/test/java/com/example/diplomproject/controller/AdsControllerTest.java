package com.example.diplomproject.controller;

import com.example.diplomproject.dto.AdsDTO;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONObject;import com.example.diplomproject.dto.CreateAds;
import com.example.diplomproject.dto.ResponseWrapperAds;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.Image;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.ImageRepository;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.AdsService;
import com.example.diplomproject.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.mock.web.MockPart;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.junit.jupiter.Testcontainers;


import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.given;

//@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
//@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdsControllerTest  {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AdsRepository repositoryAds;
    @Autowired
    private UserRepository repositoryUsers;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private CustomUserDetailsService myUserDetailsManager;
    @Autowired
    private ImageRepository repositoryImage;

    private Authentication authentication;
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    private final User users = new User();
    private final CreateAds createAds = new CreateAds();
    private final Ads ads = new Ads();
    private final Image image = new Image();


    @BeforeEach
    void setUp() {
        users.setRole(Role.USER);
        users.setFirstName("test FirstName");
        users.setLastName("test LastName");
        users.setPhone("+7123456789");
        users.setUsername("test@test.test1");
        users.setPassword(encoder.encode("test1234"));
        users.setEnabled(true);
        repositoryUsers.save(users);

        UserDetails userDetails = myUserDetailsManager
                .loadUserByUsername(users.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        ads.setTitle("Test test");
        ads.setDescription("Test test");
        ads.setPrice(150);
        ads.setAuthor(users);
        repositoryAds.save(ads);
    }

    @AfterEach
    void cleanUp() {
        repositoryAds.delete(ads);
        repositoryUsers.delete(users);
    }

    @Test
    public void testGetAllAds() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    public void testAddAds() throws Exception {
        createAds.setTitle("Test test test");
        createAds.setDescription("Add Test test");
        createAds.setPrice(150);

        MockPart created = new MockPart("properties",
                objectMapper.writeValueAsBytes(createAds));

        MvcResult result = mockMvc.perform(multipart("/ads")
                        .part(imageFile)
                        .part(created)
                        .with(authentication(authentication)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAds.getTitle()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AdsDTO createdAd = objectMapper.readValue(responseBody, AdsDTO.class);
        repositoryAds.deleteById(createdAd.getPk());
    }

    @Test
    public void testGetAdsById() throws Exception {
        mockMvc.perform(get("/ads/{id}", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ads.getId()))
                .andExpect(jsonPath("$.title").value(ads.getTitle()))
                .andExpect(jsonPath("$.description").value(ads.getDescription()))
                .andExpect(jsonPath("$.price").value(ads.getPrice()))
                .andExpect(jsonPath("$.email").value(users.getUsername()))
                .andExpect(jsonPath("$.authorFirstName").value(users.getFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(users.getLastName()))
                .andExpect(jsonPath("$.phone").value(users.getPhone()));
    }

    @Test
    public void testDeleteAd() throws Exception {
        Ads adsD = new Ads();
        adsD.setTitle("Test del");
        adsD.setDescription("Test test del");
        adsD.setPrice(250);
        adsD.setAuthor(users);
        repositoryAds.save(adsD);

        mockMvc.perform(delete("/ads/{id}", adsD.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateAd() throws Exception {
        String newTitle = "Ads test";
        String newDesc = "Test test2";
        Integer newPrice = 1555;
        ads.setTitle(newTitle);
        ads.setDescription(newDesc);
        ads.setPrice(newPrice);
        repositoryAds.save(ads);

        mockMvc.perform(patch("/ads/{id}", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ads))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle));


    }

    @Test
    public void tesGetAdsByCurrentUser() throws Exception {
        mockMvc.perform(get("/ads/me")
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray());
    }

//    @Test
//    @Transactional
//    public void testUpdateAdImage() throws Exception {
//        mockMvc.perform(patch("/ads/{id}/image", ads.getId())
//                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
//                        .with(request -> {
//                            request.addPart(imageFile);
//                            return request;
//                        })
//                        .with(authentication(authentication)))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        ads.setImage(null);
//        repositoryImage.deleteAllByBytes("image".getBytes());
//    }

    @Test
    public void testGetImage() throws Exception {
        image.setData("image".getBytes());
        repositoryImage.save(image);
        ads.setImage(image);
        repositoryAds.save(ads);

        mockMvc.perform(get("/ads/me/image/{id}", image.getId())
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(content().bytes(image.getData()));
    }
}