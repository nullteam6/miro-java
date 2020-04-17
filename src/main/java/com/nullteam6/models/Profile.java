package com.nullteam6.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIdentityInfo(property = "id", generator = ObjectIdGenerators.None.class)
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(name = "PROFILE_FRIENDS",
            joinColumns = {@JoinColumn(name = "PROFILE_ID")},
            inverseJoinColumns = {@JoinColumn(name = "FOLLOWINGLIST_ID")})
    @JsonIgnoreProperties("followingList")
    List<Profile> followingList;

    @Column(name = "user_uid", unique = true)
    private String uid;
    private String description;
    @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private AnimeBacklog aniBacklog;

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
