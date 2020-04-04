package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.Anime;
import com.nullteam6.models.AnimeBacklog;
import com.nullteam6.service.AnimeBacklogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/animebacklog")
public class AnimeBacklogController {

    @Autowired
    private AnimeBacklogDAO dao;

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public @ResponseBody
    Object getBackLog(@PathVariable String id) {
        return dao.getById(Integer.parseInt(id));
    }

    @PutMapping
    public @ResponseBody
    boolean updateBackLog(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
            dao.updateBacklog(backlog);
        } catch (JsonProcessingException ex) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    public @ResponseBody
    boolean addToBacklog(@PathVariable int id, @RequestBody String payload) {
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

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody
    boolean createBacklog(@RequestBody String payload) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
        dao.createBacklog(backlog);
        return backlog.getId() != 0;
    }
}
