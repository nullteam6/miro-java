package com.nullteam6.service;

import com.nullteam6.models.Profile;
import com.nullteam6.models.ProfileDTO;
import com.nullteam6.utility.PaginatedList;

public interface ProfileDAO {
    ProfileDTO getProfileByUID(String uid);

    boolean updateProfile(Profile profile);

    boolean createProfile(Profile profile);

    PaginatedList<Profile> search(String uid);

    PaginatedList<Profile> searchOffset(String uid, int offset);

    PaginatedList<Profile> getAll();

    PaginatedList<Profile> getAllOffset(int offset);
}
