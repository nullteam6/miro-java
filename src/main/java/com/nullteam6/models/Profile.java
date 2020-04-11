package com.nullteam6.models;

import javax.persistence.*;

@Entity
public class Profile {
    @OneToOne(fetch = FetchType.EAGER)
    AnimeBacklog aniBacklog;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "user_uid", unique = true)
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

    public AnimeBacklog getAniBacklog() {
        return aniBacklog;
    }

    public void setAniBacklog(AnimeBacklog aniBacklogList) {
        this.aniBacklog = aniBacklogList;
    }
}
