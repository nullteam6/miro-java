package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.service.UserDAOImpl;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private UserDAOImpl dao;
    private final Logger logger = LogManager.getLogger();

    @Autowired
    public void setDao(UserDAOImpl dao) {
        this.dao = dao;
    }


    @GetMapping
    public PaginatedList<User> getAllUsers() {
        return dao.getAll();
    }

    /**
     * Returns a user by username
     *
     * @param username PathVariable of the requested user
     * @return the user that is requested
     */
    @GetMapping(value = "{username}")
    public User getUser(@PathVariable String username) {
        return dao.findByUsername(username);
    }

    /**
     * DEPRECATED - this end point is now secured
     *
     * @param payload JSON representation of the UserTemplate
     * @return boolean value representing success or failure
     */
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

    /**
     * Updates a user
     *
     * @param payload JSON representation of the user to update
     * @return boolean value representing success or failure
     */
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
