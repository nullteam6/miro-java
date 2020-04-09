package com.nullteam6.controller;

import com.nullteam6.service.AnimeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/anime")
public class AnimeController {

    private AnimeService service;
    private Logger logger = LogManager.getLogger();

    @Autowired
    public void setService(AnimeService service) {
        this.service = service;
    }

    @GetMapping
    public Object getAnime(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "category", required = false) Integer category) {

        try {
            if (category != null) {
                if (offset == null)
                    return service.getByCategory(category);
                else
                    return service.getByCategoryOffset(category, offset);
            } else if (offset != null) {
                return service.downTheRabbitHole(offset);
            } else {
                return service.jeanCena();
            }
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{search}")
    public Object searchAnime(
            @PathVariable String search,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "category", required = false) Integer category) throws IOException {
        if (search.matches("[0-9]+$")) {
            return service.getById(Integer.parseInt(search));
        } else if (category == null && offset == null) {
            return service.searchForAnime(search);
        }
        return service.searchForOffset(search, offset);
    }
}

