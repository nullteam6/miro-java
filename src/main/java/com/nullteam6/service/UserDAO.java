package com.nullteam6.service;

import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;

public interface UserDAO {
    public User findByUsername(String username);

    public User registerUser(UserTemplate template);
}
