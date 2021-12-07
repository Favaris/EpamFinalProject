package com.prusan.finalproject.db.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class Activity implements Serializable {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public static class Builder {
        private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

        private Integer id;
        private String name;
        private String description;
        private int usersCount;
        private Category category;

        public Activity create() {
            Activity a = new Activity(id, name, description, usersCount, category);
            log.debug("created a new instance: {}", a);
            return a;
        }

        public Builder setId(Integer id) {
            this.id = id;
            log.debug("got set id: {}", id);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            log.debug("got set name: {}", name);
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            log.debug("got set description, length: '{}'", description == null ? null : description.length());
            return this;
        }

        public Builder setUsersCount(int usersCount) {
            this.usersCount = usersCount;
            log.debug("got set users count: {}", usersCount);
            return this;
        }

        public Builder setCategory(Category category) {
            this.category = category;
            log.debug("got set category: {}", category);
            return this;
        }
    }

    private Integer id;
    private String name;
    private String description;
    private int usersCount;
    private Category category;

    public static Activity createWithoutUsersCount(Integer id, String name, String description, Category category) {
        Activity a = new Builder().setId(id).
                setName(name).
                setDescription(description).
                setCategory(category).
                create();
        log.debug("created an activity instance without users count: {}", a);
        return a;
    }

    public static Activity createWithoutIdAndUsersCount(String name, String description, Category category) {
        Activity a = new Builder().setName(name).
                setDescription(description).
                setCategory(category).
                create();
        log.debug("created an activity instance without id and users count: {}", a);
        return a;
    }

    Activity(Integer id, String name, String description, int usersCount, Category category) {
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
