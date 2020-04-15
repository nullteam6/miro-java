package com.nullteam6.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.LoginTemplate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig(locations = "classpath:BackEnd-servlet-test.xml")
public class LoginControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void loginFalse() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LoginTemplate loginTemplate = new LoginTemplate();
        loginTemplate.setUsername("test-user");
        loginTemplate.setPassword("pasword");
        this.mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginTemplate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginTrue() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LoginTemplate loginTemplate = new LoginTemplate();
        loginTemplate.setUsername("test-user");
        loginTemplate.setPassword("password");
        this.mockMvc.perform(post("/login")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(loginTemplate))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}