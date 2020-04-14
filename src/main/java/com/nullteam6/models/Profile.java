package com.nullteam6.models;

import javax.persistence.*;
import java.util.List;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AnimeBacklog aniBacklog;

    @Column(name = "user_uid", unique = true)
    private String uid;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "PROFILE_FRIENDS")
    List<Profile> followingList;

    public Profile() {
        super();
    }

    public Profile(ProfileDTO p) {
        this.id = p.getId();
        this.aniBacklog = p.getAniBacklog();
        this.uid = p.getUid();
        this.description = p.getDescription();
        this.followingList = p.getFollowingList();
    }

    public List<Profile> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<Profile> followingList) {
        this.followingList = followingList;
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
