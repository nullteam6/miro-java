package com.nullteam6.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Profile {
    @OneToMany
    List<AnimeBacklog> aniBacklogList;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(unique = true)
    private String uid;
    private String description;

    public Profile() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<AnimeBacklog> getAniBacklogList() {
        return aniBacklogList;
    }

    public void setAniBacklogList(List<AnimeBacklog> aniBacklogList) {
        this.aniBacklogList = aniBacklogList;
    }
}
