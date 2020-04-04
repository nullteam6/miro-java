package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.service.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserDAO dao;

    @RequestMapping(value="{username}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@PathVariable String username) {
        return dao.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody boolean registerUser(@RequestBody String payload) {
        // TODO: Figure out error handling for yeeting errors
        ObjectMapper mapper = new ObjectMapper();
        UserTemplate userTemplate = null;

        try {
            userTemplate = mapper.readValue(payload, UserTemplate.class);
        } catch(JsonProcessingException e) {
            e.printStackTrace();
        }
        if (userTemplate != null) {
            dao.registerUser(userTemplate);
            return true;
        } else {
            return false;
        }
    }
}
