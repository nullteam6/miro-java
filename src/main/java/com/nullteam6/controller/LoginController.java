package com.nullteam6.controller;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.UserDTO;
import com.nullteam6.service.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@CrossOrigin
@RequestMapping("/login")
public class LoginController {

    private UserDAO userDAO;
    private final Logger logger = LogManager.getLogger();

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * DEPRECATED - Login is now handled by Keycloak
     *
     * @param loginTemplate the json representation of the LoginTemplate
     * @return the user who logged in or a HttpStatus.BAD_REQUEST
     */
    @PostMapping
    public @ResponseBody
    UserDTO login(@RequestBody LoginTemplate loginTemplate) {
        if (userDAO.authenticate(loginTemplate)) {
            return userDAO.findByUsername(loginTemplate.getUsername());
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication Failed");
    }
}
