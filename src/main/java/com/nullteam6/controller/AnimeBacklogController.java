package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.Anime;
import com.nullteam6.models.AnimeBacklog;
import com.nullteam6.service.AnimeBacklogDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<AnimeBacklog> getBackLog(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(dao.getById(id));
    }

    @PutMapping
    public ResponseEntity<Boolean> updateBackLog(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        boolean success = false;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        try {
            AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
            dao.updateBacklog(backlog);
            success = true;
            status = HttpStatus.OK;
        } catch (JsonProcessingException ex) {
            status = HttpStatus.BAD_REQUEST;
        }
        return ResponseEntity.status(status).body(success);
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
