package com.nullteam6.controller;

import com.nullteam6.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/anime")
public class AnimeController {

    @Autowired
    AnimeService service;

    @RequestMapping(value = "{search}", method = RequestMethod.GET)
    public @ResponseBody
    Object getAnime(@PathVariable String search, @RequestParam(name = "offset") int offset) throws IOException {
        if (search.matches("[0-9]+$")) {
            return service.getById(Integer.parseInt(search));
        } else {
            if (offset != 0) {
                return service.searchForOffset(search, offset);
            }
            return service.searchForAnime(search);
        }
    }
}
