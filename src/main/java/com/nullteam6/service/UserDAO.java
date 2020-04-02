package com.nullteam6.service;

import com.nullteam6.models.User;
import com.nullteam6.models.UserTemplate;

public interface UserDAO {
    
    public User findByUserName(String userName);

    public User registerUser(UserTemplate template);
}

// https://docs.google.com/document/d/1ESoj-hB1B0xvS7lPKU_V84OJvxeE6wM-dvaulryF5RQ/edit