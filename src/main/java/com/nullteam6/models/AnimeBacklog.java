package com.nullteam6.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class AnimeBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Anime> backlist;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Anime> inProgList;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Anime> finishedList;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Anime> droppedList;

    public AnimeBacklog() {
        super();
    }

    public AnimeBacklog(int id, String name, List<Anime> backlist, List<Anime> inProgList, List<Anime> finishedList, List<Anime> droppedList) {
        this.id = id;
        this.name = name;
        this.backlist = backlist;
        this.inProgList = inProgList;
        this.finishedList = finishedList;
        this.droppedList = droppedList;
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

    public List<Anime> getDroppedList() {
        return droppedList;
    }

    public void setDroppedList(List<Anime> droppedList) {
        this.droppedList = droppedList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimeBacklog that = (AnimeBacklog) o;
        return id == that.id &&
                name.equals(that.name) &&
                backlist.equals(that.backlist) &&
                inProgList.equals(that.inProgList) &&
                finishedList.equals(that.finishedList) &&
                droppedList.equals(that.droppedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, backlist, inProgList, finishedList, droppedList);
    }

    @Override
    public String toString() {
        return "AnimeBacklog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", backlist=" + backlist +
                ", inProgList=" + inProgList +
                ", finishedList=" + finishedList +
                ", droppedList=" + droppedList +
                '}';
    }
}
