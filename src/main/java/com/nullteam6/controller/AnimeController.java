package com.nullteam6.controller;

import com.nullteam6.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/anime")
public class AnimeController {

    @Autowired
    AnimeService service;

    @RequestMapping(value = "{search}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAnime(@PathVariable String search) throws IOException {
        if (search.matches("[0-9]+$")) {
            return service.getById(Integer.parseInt(search));
        } else {
            return service.searchForAnime(search);
        }
    }
}
