package com.nullteam6.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class AnimeBacklog {

    @ManyToMany(fetch = FetchType.EAGER)
    List<Anime> backlist;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Anime> inProgList;

    @ManyToMany(fetch = FetchType.EAGER)
    List<Anime> finishedList;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToOne
    private User user;

    public List<Anime> getBacklist() {
        return backlist;
    }

    public void setBacklist(List<Anime> backlist) {
        this.backlist = backlist;
    }

    public List<Anime> getInProgList() {
        return inProgList;
    }

    public void setInProgList(List<Anime> inProgList) {
        this.inProgList = inProgList;
    }

    public List<Anime> getFinishedList() {
        return finishedList;
    }

    public void setFinishedList(List<Anime> finishedList) {
        this.finishedList = finishedList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void removeFromList(Anime anime) {
        if (backlist != null)
            backlist.remove(anime);
        if (inProgList != null)
            inProgList.remove(anime);
        if (finishedList != null)
            finishedList.remove(anime);
    }

    public void addToList(Anime anime) {
        if (!backlist.contains(anime))
            backlist.add(anime);
    }
}
