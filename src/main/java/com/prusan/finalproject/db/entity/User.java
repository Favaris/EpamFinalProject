package com.prusan.finalproject.db.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 * Entity for table 'users'. Implements builder pattern.
 */
public class User implements Serializable {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    /**
     * Builder pattern implementor for User class.
     */
    public static class Builder {
        private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

        private Integer id;
        private String login;
        private String password;
        private String name;
        private String surname;
        private String role;
        private Info info;

        public User create() {
            User u = new User(id, login, password, name, surname, role, info);
            log.debug("created a new instance: {}", u);
            return u;
        }

        public Builder setPassword(String password) {
            this.password = password;
            log.debug("got set password");
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            log.debug("got set name: {}", name);
            return this;
        }

        public Builder setSurname(String surname) {
            this.surname = surname;
            log.debug("got set surname: {}", surname);
            return this;
        }

        public Builder setId(Integer id) {
            this.id = id;
            log.debug("got set id: {}", id);
            return this;
        }

        public Builder setLogin(String login) {
            this.login = login;
            log.debug("got set login: {}", login);
            return this;
        }

        public Builder setRole(String role) {
            this.role = role;
            log.debug("got set role: {}", role);
            return this;
        }

        public Builder setInfo(Info info) {
            this.info = info;
            log.debug("got set additional info: {}", info);
            return this;
        }
    }

    public static class Info {
        private int totalTime;
        private int activitiesCount;

        public Info(int totalTime, int activitiesCount) {
            this.totalTime = totalTime;
            this.activitiesCount = activitiesCount;
        }

        @Override
        public String toString() {
            return "Info{" +
                    "totalTime=" + totalTime +
                    ", activitiesCount=" + activitiesCount +
                    '}';
        }

        public int getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(int totalTime) {
            this.totalTime = totalTime;
        }

        public int getActivitiesCount() {
            return activitiesCount;
        }

        public void setActivitiesCount(int activitiesCount) {
            this.activitiesCount = activitiesCount;
        }
    }

    private Integer id;
    private String login;
    private String password;
    private String name;
    private String surname;
    private String role;
    private Info info;

    private User(Integer id, String login, String password, String name, String surname, String role, Info info) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.info = info;
    }

    public static User createUserWithoutId(String login, String password, String name, String surname, String role) {
        User u = new Builder().
                setLogin(login).
                setPassword(password).
                setName(name).
                setSurname(surname).
                setRole(role).
                create();
        log.debug("returned a user without id instance: {}", u);
        return u;
    }

    /**
     * @return a user instance without ID and with role 'user'.
     */
    public static User createDefaultUserWithoutId(String login, String password, String name, String surname) {
        User user = new Builder().
                setLogin(login).
                setPassword(password).
                setName(name).
                setSurname(surname).
                setRole("user").
                create();
        log.debug("returned a default user without id instance: {}", user);
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (!id.equals(user.id)) return false;
        return login.equals(user.login);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + login.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }
}
