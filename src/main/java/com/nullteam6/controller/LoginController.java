package com.nullteam6.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.service.UserDAO;

import com.nullteam6.utility.PBKDF2Hasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserDAO dao;

    @RequestMapping(value="{userName}", method = RequestMethod.GET)
    public @ResponseBody User getUser(@PathVariable String username) {
        return dao.findByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody User tryLogin(@RequestBody String payload) {
        // TODO: Figure out error handling for yeeting errors
        ObjectMapper mapper = new ObjectMapper();
        LoginTemplate login = null;

        try {
            login = mapper.readValue(payload, LoginTemplate.class);
            User user = dao.findByUsername(login.getUsername());

            if (user != null) {
                PBKDF2Hasher hasher = new PBKDF2Hasher();
                if (hasher.checkPassword(login.getPassword().toCharArray(), user.getPassword())) {
                    return user;
                }
            }
        } catch(JsonProcessingException e) {
            e.getStackTrace();
        }

        return null;
    }
}
