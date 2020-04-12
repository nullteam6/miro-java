package com.nullteam6.controller;

import com.nullteam6.models.Anime;
import com.nullteam6.service.AnimeService;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/anime")
@CrossOrigin
public class AnimeController {

    private AnimeService service;
    private final Logger logger = LogManager.getLogger();

    @Autowired
    public void setService(AnimeService service) {
        this.service = service;
    }

    /**
     * Returns a list of Anime, either all animes or limited by category.
     *
     * @param offset   Optional: Offset for pagination
     * @param category Optional: Category id for searching by category
     * @return PaginatedList<Anime> of the requested information
     */
    @GetMapping
    public PaginatedList<Anime> getAnime(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "category", required = false) Integer category) {

        try {
            if (category != null) {
                if (offset == null)
                    return service.getByCategory(category);
                else
                    return service.getByCategoryOffset(category, offset);
            } else if (offset != null) {
                return service.getAllWithOffset(offset);
            } else {
                return service.getAll();
            }
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get the list of trending anime
     *
     * @return JSON representation of the trending list of anime
     */
    @GetMapping(value = "/trending")
    public @ResponseBody
    List<Anime> getTrending() {
        try {
            return service.getTrending();
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /**
     * Retrieves either a single anime by id or searches for an anime by name.
     *
     * @param search PathVariable representing the name of the Anime to be searched
     * @param offset Optional: Offset for pagination
     * @return Either a PaginatedList<Anime> or a single Anime
     */
    @GetMapping(value = "/{search}")
    public Object searchAnime(
            @PathVariable String search,
            @RequestParam(name = "offset", required = false) Integer offset) {
        try {
            if (search.matches("[0-9]+$")) {
                return service.getById(Integer.parseInt(search));
            } else if (offset == null) {
                return service.searchForAnime(search);
            }
            return service.searchForOffset(search, offset);
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

