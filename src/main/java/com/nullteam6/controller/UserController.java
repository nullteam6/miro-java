package com.nullteam6.controller;

import com.nullteam6.models.User;
import com.nullteam6.models.UserDTO;
import com.nullteam6.service.UserDAOImpl;
import com.nullteam6.utility.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public UserDTO getUser(@PathVariable String username) {
        return dao.findByUsername(username);
    }
}
