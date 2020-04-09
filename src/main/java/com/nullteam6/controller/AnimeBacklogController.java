package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.Anime;
import com.nullteam6.models.AnimeBacklog;
import com.nullteam6.service.AnimeBacklogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/animebacklog")
public class AnimeBacklogController {


    private AnimeBacklogDAO dao;

    @Autowired
    public void setAnimeBacklogDAO(AnimeBacklogDAO dao) {
        this.dao = dao;
    }

    @GetMapping(value = "{id}")
    public Object getBackLog(@PathVariable String id) {
        return dao.getById(Integer.parseInt(id));
    }

    @PutMapping
    public boolean updateBackLog(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
            dao.updateBacklog(backlog);
        } catch (JsonProcessingException ex) {
            return false;
        }
        return true;
    }

    @PutMapping(value = "{id}")
    public boolean addToBacklog(@PathVariable int id, @RequestBody String payload) {
        AnimeBacklog backlog = dao.getById(id);
        ObjectMapper mapper = new ObjectMapper();
        try {
            Anime anime = mapper.readValue(payload, Anime.class);
            backlog.addToList(anime);
            dao.updateBacklog(backlog);
        } catch (JsonProcessingException ex) {
            return false;
        }
        return true;
    }

    @PostMapping
    public boolean createBacklog(@RequestBody String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
        dao.createBacklog(backlog);
        return backlog.getId() != 0;
    }
}
