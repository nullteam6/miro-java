package com.nullteam6.service;

import com.nullteam6.models.LoginTemplate;
import com.nullteam6.models.User;
import com.nullteam6.models.UserDTO;
import com.nullteam6.utility.PaginatedList;

public interface UserDAO {
    UserDTO findByUsername(String username);

    PaginatedList<User> getAll();

    PaginatedList<User> getAllOffset(int offset);

    boolean authenticate(LoginTemplate template);
}
