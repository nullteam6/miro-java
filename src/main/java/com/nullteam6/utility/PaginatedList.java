package com.nullteam6.utility;

import java.util.ArrayList;
import java.util.List;

public class PaginatedList<T> {
    private long totalCount;
    private String next;
    private String last;
    private List<T> data;

    public PaginatedList() {
        data = new ArrayList<>();
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public void add(T e) {
        data.add(e);
    }

    public void setData(List<T> e) {
        this.data = e;
    }

    public List<T> getList() {
        return data;
    }
}
