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
import com.example.diplomproject.dto.AdsDTO;
import com.example.diplomproject.dto.CreateAds;
import com.example.diplomproject.enums.Role;
import com.example.diplomproject.model.Ads;
import com.example.diplomproject.model.User;
import com.example.diplomproject.repository.AdsRepository;
import com.example.diplomproject.repository.ImageRepository;
import com.example.diplomproject.repository.UserRepository;
import com.example.diplomproject.service.CustomUserDetailsService;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureJsonTesters
class AdsControllerTest  {
    @Autowired
    MockMvc mockMvcAds;
    @Autowired
    private UserRepository userRepos;
    @Autowired
    private AdsRepository adsRepos;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CustomUserDetailsService myUserDetailsManager;
    @Autowired
    private ImageRepository imageRepository;

    private Authentication authentication;
    private final MockPart imageFile
            = new MockPart("image", "image", "image".getBytes());
    @Autowired
    private PasswordEncoder encoder;
    private final MockPart image = new MockPart("image", "image", "image".getBytes());
    private final User user = new User();

    private final Ads ads = new Ads();
    private final CreateAds createAds = new CreateAds();
    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("1@mail.ru");
        user.setFirstName("firstTest");
        user.setLastName("lastTest");
        user.setPhone("+79990002233");
        user.setPassword(encoder.encode("1234qwer"));
        user.setEnabled(true);
        user.setRole(Role.USER);
        userRepos.save(user);

        createAds.setPrice(100);
        createAds.setTitle("title");
        createAds.setDescription("description");

        ads.setPrice(200);
        ads.setDescription("about");
        ads.setTitle("title");
        ads.setAuthor(user);

        adsRepos.save(ads);



        UserDetails userDetails = myUserDetailsManager
                .loadUserByUsername(user.getUsername());
        authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(),
                userDetails.getPassword(),
                userDetails.getAuthorities());


    }

    @AfterEach
    void tearDown() {
        adsRepos.deleteAll();
        userRepos.deleteAll();
    }

    @Test
    void getAllAds() throws Exception {
        mockMvcAds.perform(get("/ads"))
                .andExpectAll(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void addAds() throws Exception {
        MockPart created = new MockPart("properties", objectMapper.writeValueAsBytes(createAds));

        mockMvcAds.perform(multipart("/ads")
                        .part(image)
                        .part(created))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAds.getTitle()))
                .andExpect(jsonPath("$.price").value(createAds.getPrice()));
    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void getAds() throws Exception {
        mockMvcAds.perform(get("/ads/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("about"))
                .andExpect(jsonPath("$.price").value(200));
    }

    @Test
    public void testDeleteAds() throws Exception {
        Ads adsD = new Ads();
        adsD.setTitle("Test del");
        adsD.setDescription("Test test del");
        adsD.setPrice(250);
        adsD.setAuthor(user);
        adsRepos.save(adsD);

        mockMvcAds.perform(delete("/ads/{id}", adsD.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isNoContent());
    }
    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void removeAds_withOtherUser() throws Exception {
        mockMvcAds.perform(delete("/ads/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer", roles = "ADMIN")
    void removeAds_withAdminRole() throws Exception {
        mockMvcAds.perform(delete("/ads/1"))
                .andExpect(status().isOk());
    }

    @Test
    void searchByTitle() throws Exception {
        mockMvcAds.perform(get("/ads/search?title=title"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.results[0].title").value("title"))
                .andExpect(jsonPath("$.count").isNumber());
    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void updateAds() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"price\": \"15\",\n" +
                                "  \"title\": \"title2\",\n" +
                                " \"description\": \"aboutNew\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title2"));

    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void updateAds_withAnotherUser() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"price\": \"15\",\n" +
                                "  \"title\": \"title2\",\n" +
                                " \"description\": \"aboutNew\"}"))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer", roles = "ADMIN")
    void updateAds_withRoleAdmin() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"price\": \"15\",\n" +
                                "  \"title\": \"title2\",\n" +
                                " \"description\": \"aboutNew\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title2"));

    }



    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void updateImage() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer")
    void updateImage_withOtherUser() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isForbidden());

    }

    @Test
    @WithMockUser(username = "2@mail.ru", password = "1234qwer", roles = "ADMIN")
    void updateImage_withRoleAdmin() throws Exception {
        mockMvcAds.perform(patch("/ads/" + ads.getId() + "/image")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.addPart(image);
                            return request;
                        }))
                .andExpect(status().isOk());

    }

    @Test
    void showImage() throws Exception {
        mockMvcAds.perform(get("/ads/image/" + ads.getId()))
                .andExpect(status().isOk())
                .andExpect(content().bytes("image".getBytes()));
    }
    @Test
    public void testGetAllAds() throws Exception {
        mockMvcAds.perform(get("/ads"))
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

        MvcResult result = mockMvcAds.perform(multipart("/ads")
                        .part(image)
                        .part(created)
                        .with(authentication(authentication)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pk").isNotEmpty())
                .andExpect(jsonPath("$.pk").isNumber())
                .andExpect(jsonPath("$.title").value(createAds.getTitle()))
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        AdsDTO createdAd = objectMapper.readValue(responseBody, AdsDTO.class);
        adsRepos.deleteById(createdAd.getPk());
    }

    @Test
    public void testGetAdsById() throws Exception {
        mockMvcAds.perform(get("/ads/{id}", ads.getId())
                        .with(authentication(authentication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pk").value(ads.getId()))
                .andExpect(jsonPath("$.title").value(ads.getTitle()))
                .andExpect(jsonPath("$.description").value(ads.getDescription()))
                .andExpect(jsonPath("$.price").value(ads.getPrice()))
                .andExpect(jsonPath("$.email").value(user.getUsername()))
                .andExpect(jsonPath("$.authorFirstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.authorLastName").value(user.getLastName()))
                .andExpect(jsonPath("$.phone").value(user.getPhone()));
    }



    @Test
    public void testUpdateAds() throws Exception {
        String newTitle = "Ads test";
        String newDesc = "Test test2";
        Integer newPrice = 1555;
        ads.setTitle(newTitle);
        ads.setDescription(newDesc);
        ads.setPrice(newPrice);
        adsRepos.save(ads);

        mockMvcAds.perform(patch("/ads/{id}", ads.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ads))
                        .with((authentication(authentication))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(newTitle));


    }

    @Test
    @WithMockUser(username = "1@mail.ru", password = "1234qwer")
    void getAdsMe() throws Exception {
        mockMvcAds.perform(get("/ads/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.results").isArray())
                .andExpect(jsonPath("$.count").isNumber());
    }
}