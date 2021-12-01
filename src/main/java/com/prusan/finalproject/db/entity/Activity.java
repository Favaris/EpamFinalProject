package com.prusan.finalproject.db.entity;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private int usersCount;
    private Category category;

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

    public Activity(Integer id, String name, String description, int usersCount, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usersCount = usersCount;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (o.getClass() == UserActivity.class) {
            UserActivity userActivity = (UserActivity) o;
            return id.equals(userActivity.getActivityId());
        }

        Activity activity = (Activity) o;

        return id.equals(activity.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", usersCount=" + usersCount +
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }
}
