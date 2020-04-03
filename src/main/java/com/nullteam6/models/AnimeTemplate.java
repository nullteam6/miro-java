package com.nullteam6.models;

import com.fasterxml.jackson.databind.JsonNode;

public class AnimeTemplate {
    private int id;
    private String type;
    private JsonNode links;
    private JsonNode attributes;
    private JsonNode relationships;

    public AnimeTemplate() {
        super();
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

    public JsonNode getLinks() {
        return links;
    }

    public void setLinks(JsonNode links) {
        this.links = links;
    }

    public JsonNode getAttributes() {
        return attributes;
    }

    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
    }

    public JsonNode getRelationships() {
        return relationships;
    }

    public void setRelationships(JsonNode relationships) {
        this.relationships = relationships;
    }
}
