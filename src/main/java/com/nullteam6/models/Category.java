package com.nullteam6.models;

public class Category {
    private int id;
    private String type;
    private String description;

    public Category() {
        super();
    }

    public Category(CategoryTemplate template) {
        this.id = template.getId();
        this.type = template.getAttributes().get("title").toString();
        this.description = template.getAttributes().get("description").toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
