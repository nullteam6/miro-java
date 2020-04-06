package com.nullteam6.controller;

import com.nullteam6.models.Category;
import com.nullteam6.service.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    private CategoryService service;
    private Logger logger = LogManager.getLogger();

    @Autowired
    public void setService(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public @ResponseBody
    List<Category> listAll() {
        List<Category> catList;
        try {
            catList = service.getAll();
        } catch (IOException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return catList;
    }

    @GetMapping(value = "/anime/{id}")
    public @ResponseBody
    List<Category> listByAnime(@PathVariable int id) {
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
