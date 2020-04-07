package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.service.UserDAOImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/user")
public class UserController {

    private UserDAOImpl dao;
    private Logger logger = LogManager.getLogger();

    @Autowired
    public void setDao(UserDAOImpl dao) {
        this.dao = dao;
    }

    @GetMapping(value = "{username}")
    public @ResponseBody
    User getUser(@PathVariable String username) {
        return dao.findByUsername(username);
    }

    @PostMapping
    public @ResponseBody
    boolean registerUser(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        UserTemplate userTemplate;

        try {
            userTemplate = mapper.readValue(payload, UserTemplate.class);
        } catch (JsonProcessingException e) {
            logger.debug(e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
        if (userTemplate != null) {
            try {
                dao.registerUser(userTemplate);
                return true;
            } catch (NoSuchAlgorithmException ex) {
                logger.debug(ex.getMessage());
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return false;
        }
    }

    @PostMapping(value = "{username}")
    public @ResponseBody
    boolean login(@PathVariable String username, @RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        LoginTemplate loginTemplate;
        boolean success = false;
        try {
            loginTemplate = mapper.readValue(payload, LoginTemplate.class);
            if (loginTemplate.getUsername().equals(username))
                success = dao.authenticate(loginTemplate);
        } catch (JsonProcessingException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        } catch (NoSuchAlgorithmException nex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return success;
    }

    @PutMapping
    public @ResponseBody
    boolean updateUser(@RequestBody String payload) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            User u = mapper.readValue(payload, User.class);
            dao.updateUser(u);
        } catch (JsonProcessingException ex) {
            return false;
        }
        return true;
    }
}
