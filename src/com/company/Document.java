package com.company;

import java.util.HashMap;
import java.util.Map;

public class Document {
    private String id;
    private String name;
    private String location;
    private Map<String, Object> tags = new HashMap<>();

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public Document(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Document(String id) {
        this.id = id;
    }

    public void addTag(String key, Object obj) {
        tags.put(key, obj);
    }
}
