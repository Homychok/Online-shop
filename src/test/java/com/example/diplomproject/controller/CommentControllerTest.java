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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.transaction.Transactional;
import java.time.Instant;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
@Transactional
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepos;
    @Autowired
    private AdsRepository adsRepos;
    @Autowired
    private CommentRepository commentRepos;
    @Autowired
    private CustomUserDetailsService manager;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvcComment;

    private final User user = new User();


    private Authentication authentication;
    private final Ads ads = new Ads();
    private final Comment comment = new Comment();
    private final CommentDTO commentDTO = new CommentDTO();

    @BeforeEach
    void setUp() {
        user.setRole(Role.USER);
        user.setFirstName("test FirstName");
        user.setLastName("test LastName");
        user.setPhone("+7123456789");
        user.setUsername("test@test.test");
        user.setPassword("test1234");
        user.setEnabled(true);
        userRepos.save(user);

        UserDetails userDetails = manager
                .loadUserByUsername(user.getUsername());

        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());

        ads.setTitle("Test ads");
        ads.setDescription("Test test");
        ads.setPrice(150);
        ads.setAuthor(user);
        adsRepos.save(ads);

        comment.setText("Test comment");
        comment.setAds(ads);
        comment.setCreatedAt(Instant.now());
        comment.setAuthor(user);
        commentRepos.save(comment);

        commentDTO.setText("Comment test");
    }

    @AfterEach
    void cleatUp() {
        commentRepos.delete(comment);
        adsRepos.delete(ads);
        userRepos.delete(user);
    }

    @Test
    public void testGetCommentsByAdId() throws Exception {
        mockMvc.perform(get("/ads/{id}/comments", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.count").isNumber())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].text").value(comment.getText()));
    }

    @Test
    public void testAddComment() throws Exception {
        MvcResult result = mockMvc.perform(post("/ads/{id}/comments", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO))
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.text")
                        .value(commentDTO.getText()))
                .andExpect(jsonPath("$.authorFirstName")
                        .value(user.getFirstName()))
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        AdsDTO createdAd = objectMapper.readValue(responseBody, AdsDTO.class);
        commentRepos.deleteById(createdAd.getPk());
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
        commentRepos.save(comment);

        mockMvc.perform(patch("/ads/{adId}/comments/{commentId}", ads.getId(), comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value(comment.getText()));
    }
    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void getComments() throws Exception {
        mockMvcComment.perform(get("/ads/" + ads.getId() + "/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void addComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setText("text2");
        mockMvcComment.perform(post("/ads/" + ads.getId() + "/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("text2"));

    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void deleteComment_withRoleUser() throws Exception {
        mockMvcComment.perform(delete("/ads/" + ads.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer", roles = "ADMIN")
    void deleteComment_withRoleAdmin() throws Exception {
        mockMvcComment.perform(delete("/ads/" + ads.getId() + "/comments/" + comment.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void updateComment() throws Exception {
        mockMvcComment.perform(patch("/ads/" + ads.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"text\": \"newText\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("newText"));
    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void updateComment_withOtherUser() throws Exception {
        mockMvcComment.perform(patch("/ads/" + ads.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"text\": \"newText\"\n" +
                                "}"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer", roles = "ADMIN")
    void updateComment_withRoleAdmin() throws Exception {
        mockMvcComment.perform(patch("/ads/" + ads.getId() + "/comments/" + comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"text\": \"newText\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("newText"));
    }}