package com.prusan.finalproject.db.entity;

/**
 * Entity class for the 'users_m2m_activities' table.
 */
public class UserActivity extends Activity {
    private Integer userId;
    private int minutesSpent;
    private boolean accepted;
    private boolean requestedAbandon;

    public UserActivity() {
    }

    public UserActivity(int userId, int activityId) {
        setActivityId(activityId);
        setUserId(userId);
    }

    public UserActivity(int minutesSpent, boolean accepted, boolean requestedAbandon) {
        this.minutesSpent = minutesSpent;
        this.accepted = accepted;
        this.requestedAbandon = requestedAbandon;
    }

    public UserActivity(Integer userId, int minutesSpent, boolean accepted, boolean requestedAbandon) {
        this.userId = userId;
        this.minutesSpent = minutesSpent;
        this.accepted = accepted;
        this.requestedAbandon = requestedAbandon;
    }

    public UserActivity(Activity ac) {
        super(ac.getId(), ac.getName(), ac.getDescription(), ac.getUsersCount(), ac.getCategory());
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
