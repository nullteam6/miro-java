package com.nullteam6.service;

import com.nullteam6.models.Anime;

public interface AnimeDAO {

    void add(Anime a);

    Anime get(int id);
}
