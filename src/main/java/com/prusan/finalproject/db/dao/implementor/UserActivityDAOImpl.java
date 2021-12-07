package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserActivityDAO;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.util.Fields;
import com.prusan.finalproject.db.util.PaginationQueries;
import com.prusan.finalproject.db.util.SQLQueries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserActivityDAOImpl extends UserActivityDAO {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    public static final String GET_REQUESTED_USERS_ACTIVITIES =
            "";
    @Override
    public void add(Connection con, UserActivity ua) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.INSERT_USER_ACTIVITY)) {
            int k = 0;
            ps.setInt(++k, ua.getUserId());
            ps.setInt(++k, ua.getActivityId());
            ps.setBoolean(++k, ua.isAccepted());
            ps.setInt(++k, ua.getMinutesSpent());
            ps.setBoolean(++k, ua.isRequestedAbandon());

            ps.executeUpdate();
            log.debug("inserted UserActivity {}", ua);
        } catch (SQLException throwables) {
            log.error("unable to insert user_activity dependence {}", ua, throwables);
            throw new DAOException(throwables);
        }
    }

    @Override
    public UserActivity get(Connection con, int userId, int activityId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.GET_USER_ACTIVITY)) {
            ps.setInt(1, userId);
            ps.setInt(2, activityId);

            rs = ps.executeQuery();
            if (rs.next()) {
                UserActivity ua = getUserActivity(rs);
                log.debug("retrieved a user activity by ids {}", ua);
                return ua;
            }
        } catch (SQLException throwables) {
            log.error("error in getUserActivity(userId={}, activityId={})", userId, activityId, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
        return null;
    }

    @Override
    public void update(Connection con, UserActivity ua) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.UPDATE_USER_ACTIVITY)) {
            int k = 0;
            ps.setBoolean(++k, ua.isAccepted());
            ps.setInt(++k, ua.getMinutesSpent());
            ps.setBoolean(++k, ua.isRequestedAbandon());
            ps.setInt(++k, ua.getActivityId());
            ps.setInt(++k, ua.getUserId());

            if (ps.executeUpdate() > 0) {
                log.debug("updated a user activity: {}", ua);
            } else {
                log.debug("did not find a user activity to update: {}", ua);
            }
        } catch (SQLException throwables) {
            log.error("unable to update user_activity {}", ua, throwables);
            throw new DAOException(throwables);
        }
    }

    @Override
    public void remove(Connection con, int userId, int activityId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.DELETE_USER_ACTIVITY)) {
            ps.setInt(1, userId);
            ps.setInt(2, activityId);
            if (ps.executeUpdate() > 0) {
                log.debug("deleted a user activity by userId={}, activityId={}", userId, activityId);
            } else {
                log.debug("unable to find a user activity by userId={}, activityId={}", userId, activityId);
            }
        } catch (SQLException throwables) {
            log.error("error in #deleteUserActivity(userId={}, activityId={})", userId, activityId, throwables);
            throw new DAOException(throwables);
        }
    }


    /**
     * Retrieves a list of all user's activities that are accepted and not requested for abandonment.
     */
    @Override
    public List<UserActivity> getAcceptedByUserId(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.GET_ALL_ACCEPTED_ACTIVITIES_BY_ID)) {
            ps.setInt(1, id);

            rs = ps.executeQuery();
            List<UserActivity> uas = new ArrayList<>();
            while (rs.next()) {
                UserActivity ua = getUserActivity(rs);
                log.debug("retrieved accepted user activity {}", ua);
                uas.add(ua);
            }
            log.debug("retrieved a list of all accepted user activities, list size: {}", uas.size());
            return uas;
        } catch (SQLException throwables) {
            log.error("error in getRunningActivities(id={})", id, throwables);
            throw new DAOException("failed to upload activities", throwables);
        }
    }


    /**
     * Returns a result of {@link #getRequestedUserActivities(Connection, Integer, int, int)} with a null for the userId parameter.
     */
    @Override
    public List<UserActivity> getRequestedUserActivities(Connection con, int limit, int offset) throws DAOException {
        return getRequestedUserActivities(con, null, limit, offset);
    }

    /**
     * Returns a list of user activities that are not accepted or requested for abandonment for user specified by a userId.<br>
     * If a userId param was null, returns a list of all requested user activities for all users.
     * @param userId id for whom to download the requests
     */
    @Override
    public List<UserActivity> getRequestedUserActivities(Connection con, Integer userId, int limit, int offset) throws DAOException {
        String query = SQLQueries.UserActivityQueries.GET_REQUESTED_USERS_ACTIVITIES;
        if (userId == null) {
            query = String.format(query, "");
            log.debug("userId was null, setting the query without userId filtering");
        } else {
            query = String.format(query, "AND ua_user_id = ?");
            log.debug("userId was {}, setting the query with userId filtering", userId);
        }
        log.debug("query: '{}'", query);

        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            int k = 0;
            if (userId != null) {
                ps.setInt(++k,  userId);
            }
            ps.setInt(++k, limit);
            ps.setInt(++k, offset);

            rs = ps.executeQuery();
            List<UserActivity> uas = new ArrayList<>();
            while (rs.next()) {
                UserActivity ua = getUserActivity(rs);
                log.debug("retrieved a user activity {}", ua);
                uas.add(ua);
            }
            log.debug("retrieved a list of requested users' activities, list size: {}", uas.size());
            return uas;
        } catch (SQLException throwables) {
            log.error("unable to retrieve all requested user activities");
            throw new DAOException("unable to retrieve all requested user activities", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
    }

    /**
     * Returns a result of {@link #getRequestsCount(Connection, Integer)} with a null for the userId parameter.
     */
    @Override
    public int getRequestsCount(Connection con) throws DAOException {
        return getRequestsCount(con, null);
    }

    /**
     * Returns a count of requests for specified user id. If id was null, returns count of all users' requests.
     */
    @Override
    public int getRequestsCount(Connection con, Integer userId) throws DAOException {
        String query = SQLQueries.UserActivityQueries.GET_REQUESTS_COUNT;
        if (userId == null) {
            query = String.format(query, "");
            log.debug("user id was null, set a query for counting all users' requests");
        } else {
            query = String.format(query, "AND ua_user_id = ?");
            log.debug("user id was {}, set a query for counting this user's requests", userId);
        }
        log.debug("query: '{}'", query);
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(query)) {
            if (userId != null) {
                ps.setInt(1, userId);
            }

            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("retrieved a count of requests for user id={}", userId);
            return count;
        } catch (SQLException throwables) {
            log.error("failed to get count of requests for userId={}", userId);
            throw new DAOException("failed to get count of requests", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
    }

    @Override
    public void removeAllByUserId(Connection con, int userId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.DELETE_ALL_BY_USER_ID)){
            ps.setInt(1, userId);
            if (ps.executeUpdate() > 0) {
                log.debug("successfully deleted all user's activities by userId={}", userId);
            } else {
                log.debug("there were no activities to delete for user with userId={}", userId);
            }
        } catch (SQLException throwables) {
            log.error("failed to remove all user's activities by userId={}", userId, throwables);
            throw new DAOException("unable to delete user's activities", throwables);
        }
    }

    @Override
    public int getCountByUserId(Connection con, int userId, String[] filterBy) throws DAOException {
        ResultSet rs = null;
        String query;
        if ("all".equals(filterBy[0])) {
            query = SQLQueries.UserActivityQueries.GET_ALL_COUNT_BY_USER_ID;
        } else {
            query = String.format(SQLQueries.UserActivityQueries.GET_FILTERED_COUNT_BY_USER_ID, String.join(",", filterBy));
        }
        log.debug("got a query for user activities count: '{}'", query);
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("retrieved a count of all user's activities, count={}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("failed to get count of all activities for user with id={}", userId, throwables);
            throw new DAOException("failed to get count of user's activities with userId=" + userId, throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
    }

    @Override
    public int getSummarizedSpentTimeByUserId(Connection con, int userId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(SQLQueries.UserActivityQueries.GET_SUM_OF_MINUTES_BY_USER_ID)) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            int sum = 0;
            if (rs.next()) {
                sum = rs.getInt(1);
            }
            log.debug("retrieved a summarized time spent on activities by user with id={}", userId);
            return sum;
        } catch (SQLException throwables) {
            log.error("failed to get a sum of all minutes spent on activities by userId={}", userId, throwables);
            throw new DAOException("failed to get a total sum of time spent by user with id=" + userId, throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
    }

    @Override
    public List<UserActivity> getAcceptedByUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(PaginationQueries.getUserActivityQuery(orderBy, filterBy))) {
            int k = 0;
            ps.setInt(++k, userId);
            ps.setInt(++k, limit);
            ps.setInt(++k, offset);

            rs = ps.executeQuery();
            List<UserActivity> userActivities = new ArrayList<>();
            while (rs.next()) {
                UserActivity ua = getUserActivity(rs);
                userActivities.add(ua);
                log.debug("added a user activity {} to the list", ua);
            }
            log.debug("retrieved a list of user activities for user #{}, limit={}, offset={}", limit, limit, offset);

            return userActivities;
        } catch (SQLException throwables) {
            log.error("failed to get a list of user activities by userId={}, limit={}, offset={}", limit, limit, offset, throwables);
            throw new DAOException("failed to get a list of user activities", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close the result set {}", rs, e);
            }
        }
    }

    private UserActivity getUserActivity(ResultSet rs) throws SQLException {
        Category c = new Category(rs.getInt(Fields.ACTIVITY_CATEGORY_ID), rs.getString(Fields.CATEGORY_NAME));
        UserActivity.Builder builder = new UserActivity.Builder();

        UserActivity ua = builder.
                setId(rs.getInt(Fields.ACTIVITY_ID)).
                setName(rs.getString(Fields.ACTIVITY_NAME)).
                setDescription(rs.getString(Fields.ACTIVITY_DESCRIPTION)).
                setUsersCount(rs.getInt(Fields.ACTIVITY_USERS_COUNT)).
                setCategory(c).
                setUserId(rs.getInt(Fields.USER_ACTIVITY_USER_ID)).
                setMinutesSpent(rs.getInt(Fields.USER_ACTIVITY_MINUTES_SPENT)).
                setAccepted(rs.getBoolean(Fields.USER_ACTIVITY_ACCEPTED)).
                setRequestedAbandon(rs.getBoolean(Fields.USER_ACTIVITY_REQUESTED_ABANDON)).
                create();
        log.debug("retrieved a user activity: {}", ua);
        return ua;
    }
}
