package com.nullteam6.service;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.models.UserDTO;
import com.nullteam6.models.UserTemplate;
import com.nullteam6.utility.PaginatedList;

import java.security.NoSuchAlgorithmException;

public interface UserDAO {
    UserDTO findByUsername(String username);

    boolean registerUser(UserTemplate template) throws NoSuchAlgorithmException;

    PaginatedList<User> getAll();

    PaginatedList<User> getAllOffset(int offset);

    void updateUser(User user);

    boolean authenticate(LoginTemplate template) throws NoSuchAlgorithmException;
}
