package com.nullteam6.utility;

import com.fasterxml.jackson.databind.JsonNode;

import java.net.URL;

public class KitsuCommand {
    private JsonNode payload;
    private URL url;
    private boolean completed;

    public KitsuCommand(JsonNode payload, URL url) {
        this.payload = payload;
        this.url = url;
        this.completed = false;
    }

    public synchronized JsonNode getPayload() {
        return payload;
    }

    public synchronized void setPayload(JsonNode payload) {
        this.payload = payload;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public synchronized boolean isCompleted() {
        return completed;
    }

    public synchronized void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
