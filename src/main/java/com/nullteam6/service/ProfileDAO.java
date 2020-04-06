package com.nullteam6.service;

import com.nullteam6.models.Profile;

public interface ProfileDAO {
    Profile getProfileByUID(String uid);

    boolean updateProfile(Profile profile);

    boolean createProfile(Profile profile);
}
