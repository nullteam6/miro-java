package com.nullteam6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.ProfileDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig(locations = "classpath:BackEnd-servlet-test.xml")
public class ProfileControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getAllProfileAuthorized() throws Exception {
        this.mockMvc.perform(get("/profile")
                .accept(MediaType.APPLICATION_JSON).with(user("grturner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getAllProfileUnauthorized() throws Exception {
        this.mockMvc.perform(get("/profile").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAllProfilesOffset() throws Exception {
        this.mockMvc.perform(get("/profile?offset=1")
                .with(user("grturner").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getByUIDAuthorized() throws Exception {
        this.mockMvc.perform(get("/profile/grturner")
                .accept(MediaType.APPLICATION_JSON)
                .with(user("grturner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getByUIDUnauthorized() throws Exception {
        this.mockMvc.perform(get("/profile/grturner").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void searchProfileAuthorized() throws Exception {
        this.mockMvc.perform(get("/profile?search=grturner")
                .accept(MediaType.APPLICATION_JSON)
                .with(user("grturner").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void searchProfileUnauthorized() throws Exception {
        this.mockMvc.perform(get("/profile?search=grturner").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void searchProfileOffset() throws Exception {
        this.mockMvc.perform(get("/profile?search=grturner&offset=0")
                .with(user("grturner").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void updateProfileAuthorized() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(get("/profile/grturner")
                .with(user("grturner").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ProfileDTO profileDTO = mapper.readValue(json, ProfileDTO.class);

        this.mockMvc.perform(put("/profile")
                .accept(MediaType.APPLICATION_JSON)
                .with(user("grturner").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profileDTO)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateProfileUnauthorized() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = this.mockMvc.perform(get("/profile/grturner")
                .with(user("grturner").roles("USER"))
                .accept(MediaType.APPLICATION_JSON))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        ProfileDTO profileDTO = mapper.readValue(json, ProfileDTO.class);

        this.mockMvc.perform(put("/profile")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(profileDTO)))
                .andExpect(status().isUnauthorized());
    }
}