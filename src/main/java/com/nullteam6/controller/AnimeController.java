package com.nullteam6.controller;

import com.nullteam6.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/anime")
public class AnimeController {

    @Autowired
    AnimeService service;

    @GetMapping
    public @ResponseBody
    Object getAnime(
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "category", required = false) Integer category
    ) throws Exception {
        if (category != null && offset == null) {
            return service.getByCategory(category);
        } else if (category != null && offset != null) {
            return service.getByCategoryOffset(category, offset);
        } else if (category == null && offset != null) {
            return service.downTheRabbitHole(offset);
        } else {
            return service.jeanCena();
        }
    }

    @GetMapping(value = "{search}")
    public @ResponseBody
    Object searchAnime(
            @PathVariable String search,
            @RequestParam(name = "offset", required = false) Integer offset,
            @RequestParam(name = "category", required = false) Integer category) throws Exception {
        if (search.matches("[0-9]+$")) {
            return service.getById(Integer.parseInt(search));
        } else if (category == null && offset == null) {
            return service.searchForAnime(search);
        }
        return service.searchForOffset(search, offset);
    }
}

