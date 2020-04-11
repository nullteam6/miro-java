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

@RestController
@RequestMapping("/animebacklog")
public class AnimeBacklogController {


    private AnimeBacklogDAO dao;

    @Autowired
    public void setAnimeBacklogDAO(AnimeBacklogDAO dao) {
        this.dao = dao;
    }

    /**
     * Returns JSON representation of an AnimeBacklog
     *
     * @param id the id of the AnimeBacklog to retrieve
     * @return JSON representation of that AnimeBacklog
     */
    @GetMapping(value = "{id}")
    public ResponseEntity<AnimeBacklog> getBackLog(@PathVariable int id) {
        return ResponseEntity.status(HttpStatus.OK).body(dao.getById(id));
    }

    /**
     * Reads in a backlog from JSON and updates it in the database.
     *
     * @param payload the JSON representation of the backlog to update
     * @return boolean value indicating success or failure
     */
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

//    /**
//     * Adds a specific anime to a backlog.
//     *
//     * @param id      - the id of the BackLog to add the anime to
//     * @param payload - the JSON object of the anime
//     * @return boolean value indicating a success or failure
//     */
//    @PutMapping(value = "{id}")
//    public boolean addToBacklog(@PathVariable int id, @RequestBody String payload) {
//        AnimeBacklog backlog = dao.getById(id);
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            Anime anime = mapper.readValue(payload, Anime.class);
//            backlog.addToList(anime);
//            dao.updateBacklog(backlog);
//        } catch (JsonProcessingException ex) {
//            return false;
//        }
//        return true;
//    }

    /**
     * Creates a new AnimeBacklog
     *
     * @param payload JSON representation of an AnimeBacklog to create
     * @return boolean value representing success or failure
     * @throws JsonProcessingException if payload is malformed
     */
    @PostMapping
    public boolean createBacklog(@RequestBody String payload) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        AnimeBacklog backlog = mapper.readValue(payload, AnimeBacklog.class);
        dao.createBacklog(backlog);
        return backlog.getId() != 0;
    }
}
