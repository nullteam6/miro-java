package com.nullteam6.service;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;

import java.security.NoSuchAlgorithmException;

public interface UserDAO {
    User findByUsername(String username);

    boolean registerUser(UserTemplate template) throws NoSuchAlgorithmException;

    void updateUser(User user);

    boolean authenticate(LoginTemplate template) throws NoSuchAlgorithmException;
}
