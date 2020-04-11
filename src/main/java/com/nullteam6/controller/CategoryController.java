package com.nullteam6.controller;

import com.nullteam6.models.Category;
import com.nullteam6.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private CategoryService service;
    private final Logger logger = LogManager.getLogger();

    @Autowired
    public void setService(CategoryService service) {
        this.service = service;
    }

    /**
     * Returns all categories.
     *
     * @return a list of all categories
     */
    @GetMapping
    public List<Category> listAll() {
        List<Category> catList;
        try {
            catList = service.getAll();
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return catList;
    }

    /**
     * Returns all the categories associated with an anime
     *
     * @param id the id of the anime
     * @return the list of categories that anime has been tagged with
     */
    @GetMapping(value = "/anime/{id}")
    public List<Category> listByAnime(@PathVariable int id) {
        List<Category> catList;
        try {
            catList = service.getByAnime(id);
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return catList;
    }
}
