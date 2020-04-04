package com.nullteam6.service;

import com.nullteam6.models.AnimeBacklog;

public interface AnimeBacklogDAO {

    AnimeBacklog getById(int id);

    void createBacklog(AnimeBacklog backlog);

    void updateBacklog(AnimeBacklog backlog);

}
