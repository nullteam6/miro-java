package com.nullteam6.models;

import java.util.List;

public class ProfileDTO {
    List<Profile> followingList;
    private int id;
    private AnimeBacklog aniBacklog;
    private String uid;
    private String description;

    public ProfileDTO(Profile p) {
        this.id = p.getId();
        this.aniBacklog = p.getAniBacklog();
        this.uid = p.getUid();
        this.description = p.getUid();
        this.description = p.getDescription();
        this.followingList = p.getFollowingList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AnimeBacklog getAniBacklog() {
        return aniBacklog;
    }

    public void setAniBacklog(AnimeBacklog aniBacklog) {
        this.aniBacklog = aniBacklog;
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

    public List<Profile> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<Profile> followingList) {
        this.followingList = followingList;
    }
}
