package com.prusan.finalproject.db.entity;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private List<Category> categories;

    public Activity() {
    }

    public Activity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Activity(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
