package com.nullteam6.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringJUnitWebConfig(locations = "classpath:BackEnd-servlet-test.xml")
public class CategoryControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void listAll() throws Exception {
        URL url = new URL("https://kitsu.io/api/edge/categories");
        JsonNode node = getJson(url);
        JsonNode arr = node.get("data");
        List<String> nameList = new ArrayList<>();
        for (JsonNode n : arr) {
            nameList.add(n.get("attributes").get("title").toString());
        }
        this.mockMvc.perform(get("/category").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value(nameList.get(0)));
    }

    @Test
    public void listByAnime() throws Exception {
        URL url = new URL("https://kitsu.io/api/edge/anime/21/categories");
        JsonNode node = getJson(url);
        JsonNode arr = node.get("data");
        List<String> namesList = new ArrayList<>();
        for (JsonNode n : arr) {
            namesList.add(n.get("attributes").get("title").toString());
        }
        this.mockMvc.perform(get("/category/anime/21").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].type").value(namesList.get(0)));
    }

    private JsonNode getJson(URL url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/vnd.api+json");
        con.setRequestProperty("ContentType", "application/vnd.api+json");
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(content.toString());
    }
}