package com.example.diplomproject.controller;

import com.example.diplomproject.dto.AdsDTO;
import com.example.diplomproject.dto.CommentDTO;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.Comment;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.CommentRepository;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CustomUserDetailsService manager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;

    private Authentication authentication;
    private final User users = new User();
    private final Ads ads = new Ads();
    private final Comment comment = new Comment();
    private final CommentDTO commentDTO = new CommentDTO();

    @BeforeEach
    void setUp() {
        users.setRole(Role.USER);
        users.setFirstName("test FirstName");
        users.setLastName("test LastName");
        users.setPhone("+7123456789");
        users.setUsername("test@test.test");
        users.setPassword("test1234");
        users.setEnabled(true);
        userRepository.save(users);

        UserDetails userDetails = manager
                .loadUserByUsername(users.getUsername());

        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        ads.setTitle("Test ads");
        ads.setDescription("Test test");
        ads.setPrice(150);
        ads.setAuthor(users);
        adsRepository.save(ads);

        comment.setText("Test comment");
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(users);
        commentRepository.save(comment);

        commentDTO.setText("Comment test");
    }

    @AfterEach
    void deleteAll() {
        commentRepository.delete(comment);
        adsRepository.delete(ads);
        userRepository.delete(users);
    }

    @Test
    public void testGetCommentsById() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }

    @Test
    public void testAddAdsComment() throws Exception {
        MvcResult result = mockMvc.perform(post("/ads/{id}/comments", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.text")
                        .value(commentDTO.getText()))
                .andExpect(jsonPath("$.authorFirstName")
                        .value(users.getFirstName()))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        AdsDTO createdAd = objectMapper.readValue(responseBody, AdsDTO.class);
        commentRepository.deleteById(createdAd.getPk());
    }

    @Test
    public void testDeleteComment() throws Exception {
        mockMvc.perform(delete("/ads/{adId}/comments/{commentId}", ads.getId(), comment.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateComment() throws Exception {
        String newText = "New Test comment";
        comment.setText(newText);
        commentRepository.save(comment);

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", ads.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(comment.getText()));
    }
}