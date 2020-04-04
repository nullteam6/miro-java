package com.nullteam6.service;

import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;

public interface UserDAO {
    User findByUsername(String username);

    User registerUser(UserTemplate template);

    void updateUser(User user);
}
