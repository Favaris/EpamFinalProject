package com.prusan.finalproject.db.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Entity class for the 'users_m2m_activities' table. Implements builder pattern.
 */
public class UserActivity extends Activity {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public static class Builder extends Activity.Builder {
        private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

        private Integer userId;
        private int minutesSpent;
        private boolean accepted;
        private boolean requestedAbandon;

        public UserActivity create() {
            Activity a = super.create();
            UserActivity ua = new UserActivity(a, userId, minutesSpent, accepted, requestedAbandon);
            log.debug("created an instance: {}", ua);
            return ua;
        }

        public Builder setUserId(Integer userId) {
            this.userId = userId;
            log.debug("got set userId: {}", userId);
            return this;
        }

        public Builder setMinutesSpent(int minutesSpent) {
            this.minutesSpent = minutesSpent;
            log.debug("got set minutesSpent: {}", minutesSpent);
            return this;
        }

        public Builder setAccepted(boolean accepted) {
            this.accepted = accepted;
            log.debug("got set accepted: {}", accepted);
            return this;
        }

        public Builder setRequestedAbandon(boolean requestedAbandon) {
            this.requestedAbandon = requestedAbandon;
            log.debug("got set requestedAbandon: {}", requestedAbandon);
            return this;
        }

        public Builder setActivityId(Integer activityId) {
            super.setId(activityId);
            return this;
        }

        @Override
        public Builder setId(Integer id) {
            super.setId(id);
            return this;
        }

        @Override
        public Builder setName(String name) {
            super.setName(name);
            return this;
        }

        @Override
        public Builder setDescription(String description) {
            super.setDescription(description);
            return this;
        }

        @Override
        public Builder setUsersCount(int usersCount) {
            super.setUsersCount(usersCount);
            return this;
        }

        @Override
        public Builder setCategory(Category category) {
            super.setCategory(category);
            return this;
        }
    }

    private Integer userId;
    private int minutesSpent;
    private boolean accepted;
    private boolean requestedAbandon;

    public static UserActivity createAcceptedWithIds(Integer userId, Integer activityId) {
        UserActivity ua = new Builder().
                setUserId(userId).
                setActivityId(activityId).
                setAccepted(true).
                create();
        log.debug("created an instance with accepted=true and ids: {}", ua);
        return ua;
    }

    public static UserActivity createStubWithIds(Integer userId, Integer activityId) {
        UserActivity ua = new Builder().
                setUserId(userId).
                setActivityId(activityId).
                create();
        log.debug("created a stub only ids: {}", ua);
        return ua;
    }

    private UserActivity(Activity ac, Integer userId, int minutesSpent, boolean accepted, boolean requestedAbandon) {
        super(ac.getId(), ac.getName(), ac.getDescription(), ac.getUsersCount(), ac.getCategory());
        this.userId = userId;
        this.minutesSpent = minutesSpent;
        this.accepted = accepted;
        this.requestedAbandon = requestedAbandon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o.getClass() == super.getClass()) {
            return super.equals(o);
        }

        UserActivity that = (UserActivity) o;

        return userId.equals(that.userId) && getActivityId().equals(that.getActivityId());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserActivity{" +
                "userId=" + userId +
                ", minutesSpent=" + minutesSpent +
                ", accepted=" + accepted +
                ", requestedAbandon=" + requestedAbandon +
                "} " + super.toString();
    }

    /**
     * Getter method for convenience. Basically the same as Activity#getId().
     * @return super.getId()
     */
    public Integer getActivityId() {
        return getId();
    }

    /**
     * Setter method for convenience. Basically the same as Activity#setId().
     * @param activityId id
     */
    public void setActivityId(Integer activityId) {
        setId(activityId);
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getMinutesSpent() {
        return minutesSpent;
    }

    public void setMinutesSpent(int minutesSpent) {
        this.minutesSpent = minutesSpent;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isRequestedAbandon() {
        return requestedAbandon;
    }

    public void setRequestedAbandon(boolean requestedAbandon) {
        this.requestedAbandon = requestedAbandon;
    }
}
