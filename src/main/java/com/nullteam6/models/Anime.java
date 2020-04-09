package com.nullteam6.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Anime {

    @Id
    private int id;

    @Column(columnDefinition = "NVARCHAR2(255)")
    private String name;

    @Column(columnDefinition = "NVARCHAR2(2000)")
    private String synopsis;

    private String logo;
    private String episodeCount;
    private String showType;
    private String status;
    private String startDate;
    private String endDate;

    public Anime() {
        super();
    }

    public Anime(AnimeTemplate template) {
        this.id = template.getId();
        this.name = template.getAttributes().get("canonicalTitle").toString();
        this.synopsis = template.getAttributes().get("synopsis").toString();
        if (!template.getAttributes().get("posterImage").isNull())
            this.logo = template.getAttributes().get("posterImage").get("original").toString();
        this.episodeCount = template.getAttributes().get("episodeCount").toString();
        this.showType = template.getAttributes().get("showType").toString();
        this.status = template.getAttributes().get("status").toString();
        this.startDate = template.getAttributes().get("startDate").toString();
        this.endDate = template.getAttributes().get("endDate").toString();
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getEpisodeCount() {
        return episodeCount;
    }

    public void setEpisodeCount(String episodeCount) {
        this.episodeCount = episodeCount;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
