package com.nullteam6.models;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class AnimeBacklog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.PERSIST, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "BACKLOG_ANIME")
    private List<Anime> backlist;


    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.PERSIST, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "PROGRESS_ANIME")
    private List<Anime> inProgList;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.PERSIST, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "FINISHED_ANIME")
    private List<Anime> finishedList;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.PERSIST, CascadeType.REMOVE})
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "DROPPED_ANIME")
    private List<Anime> droppedList;

    public AnimeBacklog() {
        super();
    }

    public AnimeBacklog(int id, List<Anime> backlist, List<Anime> inProgList, List<Anime> finishedList, List<Anime> droppedList) {
        this.id = id;
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
                backlist.equals(that.backlist) &&
                inProgList.equals(that.inProgList) &&
                finishedList.equals(that.finishedList) &&
                droppedList.equals(that.droppedList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, backlist, inProgList, finishedList, droppedList);
    }

    @Override
    public String toString() {
        return "AnimeBacklog{" +
                "id=" + id +
                ", backlist=" + backlist +
                ", inProgList=" + inProgList +
                ", finishedList=" + finishedList +
                ", droppedList=" + droppedList +
                '}';
    }
}
