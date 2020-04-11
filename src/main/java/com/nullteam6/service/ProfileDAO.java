package com.nullteam6.service;

import com.nullteam6.models.Profile;
import com.nullteam6.utility.PaginatedList;

public interface ProfileDAO {
    Profile getProfileByUID(String uid);

    boolean updateProfile(Profile profile);

    boolean createProfile(Profile profile);

    PaginatedList<Profile> getAll();

    PaginatedList<Profile> getAllOffset(int offset);
}
