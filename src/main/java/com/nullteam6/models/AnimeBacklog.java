package com.nullteam6.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class AnimeBacklog {

    @ManyToMany(fetch = FetchType.EAGER)
    List<Anime> animelist;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToOne
    private User user;

    public AnimeBacklog() {
        super();
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

    public List<Anime> getAnimelist() {
        return animelist;
    }

    public void setAnimelist(List<Anime> animelist) {
        this.animelist = animelist;
    }

}
