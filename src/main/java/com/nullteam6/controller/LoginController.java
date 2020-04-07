package com.nullteam6.controller;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.service.UserDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/login")
public class LoginController {

    private UserDAO userDAO;
    private Logger logger = LogManager.getLogger();

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @PostMapping
    public @ResponseBody
    User login(@RequestBody LoginTemplate loginTemplate) {
        try {
            if (userDAO.authenticate(loginTemplate)) {
                return userDAO.findByUsername(loginTemplate.getUsername());
            }
        } catch (NoSuchAlgorithmException ex) {
            logger.debug(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Authentication Failed");
    }
}
