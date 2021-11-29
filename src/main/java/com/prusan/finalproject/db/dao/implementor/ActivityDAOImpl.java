package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.util.Fields;
import com.prusan.finalproject.db.util.PaginationQueries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * a DAO layer for Activity entity.
 */
public class ActivityDAOImpl extends ActivityDAO {
    private static final Logger log = LogManager.getLogger(ActivityDAOImpl.class);

    public static final String GET_ACTIVITY_BY_NAME = "SELECT * FROM activities, categories WHERE a_name = ? AND c_id = a_category_id";
    public static final String INSERT_ACTIVITY = "INSERT INTO activities(a_name, a_description, a_category_id) VALUES (?,?,?)";
    public static final String GET_ACTIVITY_BY_ID = "SELECT * FROM activities, categories WHERE a_id = ?";
    public static final String UPDATE_ACTIVITY_BY_ID = "UPDATE activities SET a_name = ?, a_description = ?, a_category_id = ? WHERE a_id = ?";
    public static final String DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE a_id = ?";
    public static final String GET_ALL_ACTIVITIES = "SELECT * FROM activities, categories WHERE a_category_id = c_id";


    public static final String INSERT_USER_ACTIVITY = "INSERT INTO users_m2m_activities VALUES (?, ?, ?, ?, ?)";
    public static final String GET_REQUESTED_USERS_ACTIVITIES =
            "SELECT * FROM users_m2m_activities, activities, categories WHERE (ua_accepted = 0 OR ua_requested_abandon = 1) AND a_id = ua_activity_id AND c_id = a_category_id";
    public static final String UPDATE_USER_ACTIVITY = "UPDATE users_m2m_activities SET ua_accepted = ?, ua_minutes_spent = ?, ua_requested_abandon = ? WHERE ua_activity_id = ? AND ua_user_id = ?";
    public static final String DELETE_USER_ACTIVITY = "DELETE FROM users_m2m_activities WHERE ua_user_id = ? AND ua_activity_id = ?";
    public static final String GET_USER_ACTIVITY =
            "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_activity_id = ? AND ua_activity_id = a_id AND a_category_id = c_id";
    public static final String GET_COUNT_OF_AVAILABLE_ACTIVITIES_FOR_USER = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?)";
    public static final String GET_AVAILABLE_ACTIVITIES_COUNT_WITH_FILTERS = "SELECT COUNT(*) FROM activities WHERE a_id NOT IN (SELECT ua_activity_id FROM users_m2m_activities WHERE ua_user_id = ?) AND a_category_id IN (%s)";

    @Override
    public Activity getByName(Connection con, String name) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ACTIVITY_BY_NAME)) {
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                return getActivity(rs);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #getByName(name={})", name, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.warn("unable to close a resource: {}", rs, ex);
            }
        }
        log.debug("unable to find activity by name={}", name);
        return null;
    }


    /**
     * Returns a list of activities sorted by a given entity name in ASC order.
     * @param limit start position
     * @param offset end position
     * @param orderBy name of a certain attr in the 'activities' table to be sorted by
     */
    @Override
    public List<Activity> getActivities(Connection con, int limit, int offset, String orderBy, String... filterBy) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(PaginationQueries.getActivityQuery(orderBy, filterBy))) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            rs = ps.executeQuery();
            List<Activity> activities = new ArrayList<>();
            while (rs.next()) {
                Activity ac = getActivity(rs);
                activities.add(ac);
                log.debug("placed an activity {} into the list", ac);
            }
            log.debug("returned a result list with size={}", activities.size());

            return activities;
        } catch (SQLException throwables) {
            log.error("failed to get a sorted part of activities, limit={}, offset={}, orderBy={}", limit, offset, orderBy, throwables);
            throw new DAOException("Can not get a list of sorted activities", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.error("unable to close a result set {}", rs, e);
            }
        }
    }

    /**
     * Returns a number of all activities.
     */
    @Override
    public int getCount(Connection con, int userId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_COUNT_OF_AVAILABLE_ACTIVITIES_FOR_USER)) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("retrieved activity count={}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("failed to get count of all activities", throwables);
            throw  new DAOException("unable to get activities count", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.error("unable to close a result set {}", rs, e);
            }
        }
    }

    @Override
    public void addUserActivity(Connection con, UserActivity ua) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_USER_ACTIVITY)) {
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
    public UserActivity getUserActivity(Connection con, int userId, int activityId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_USER_ACTIVITY)) {
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
    public void updateUserActivity(Connection con, UserActivity ua) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_USER_ACTIVITY)) {
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
    public void deleteUserActivity(Connection con, int userId, int activityId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_USER_ACTIVITY)) {
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
     * Returns a list of all UserActvities that are not accepted yet or requested for abandonment
     */
    @Override
    public List<UserActivity> getRequestedUserActivities(Connection con) throws DAOException {
        List<UserActivity> uas = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_REQUESTED_USERS_ACTIVITIES)) {
            while (rs.next()) {
                UserActivity ua = getUserActivity(rs);
                log.debug("retrieved a user activity {}", ua);
                uas.add(ua);
            }
        } catch (SQLException throwables) {
            log.error("unable to retrieve all requested user activities");
            throw new DAOException("unable to retrieve all requested user activities", throwables);
        }

        return uas;
    }

    /**
     * Gets a list of Activities that are already taken by the given user. Activity is 'taken' even if it is not yet accepted by an admin.
     * @throws DAOException if connection error occurs.
     */
    @Override
    public List<Activity> getAvailableActivitiesForUserId(Connection con, int userId, int limit, int offset, String orderBy, String... filterBy) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(PaginationQueries.getActivityQueryForUser(orderBy, filterBy))) {
            int k = 0;
            ps.setInt(++k, userId);
            ps.setInt(++k, limit);
            ps.setInt(++k, offset);
            rs = ps.executeQuery();
            List<Activity> result = new ArrayList<>();
            while (rs.next()) {
                Activity a = getActivity(rs);
                log.debug("retrieved activity by user id={}, activity={}", userId, a);
                result.add(a);
            }
            log.debug("retrieved a list of all activities, available to a user with id={}, list size: {}", userId, result.size());
            return result;
        } catch (SQLException throwables) {
            log.error("unable to download all activities available to a user with id={}", userId, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close a resource {}", rs, e);
            }
        }
    }

    @Override
    public int getFilteredCount(Connection con, int userId, String... filterBy) throws DAOException {
        String filters = String.join(",", filterBy);
        log.debug("got a string of filters '{}'", filters);
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(String.format(GET_AVAILABLE_ACTIVITIES_COUNT_WITH_FILTERS, filters))) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }
            log.debug("retrieved a number of activities filtered by '{}', count={}", filters, count);
            return count;
        } catch (SQLException throwables) {
            log.error("failed to get a number of activities by filter '{}'", filters, throwables);
            throw new DAOException("failed to get a number of activities", throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close a resource {}", rs, e);
            }
        }
    }

    /**
     * Inserts a new activity to the db. If insertion was successful, updates the generated id field for given Activity object.
     * @param con connection with db
     * @param activity new activity to insert
     * @throws DAOException if activity can not be inserted; or caught some troubles on connection/statement execution.
     */
    @Override
    public void add(Connection con, Activity activity) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(INSERT_ACTIVITY, Statement.RETURN_GENERATED_KEYS)) {
            int k = 0;
            ps.setString(++k, activity.getName());
            ps.setString(++k, activity.getDescription());
            ps.setInt(++k, activity.getCategory().getId());

            if (ps.executeUpdate() > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    activity.setId(rs.getInt(1));
                    log.debug("inserted a new activity {}", activity);
                }
            }
        } catch (SQLException throwables) {
            log.debug("unable to insert an activity {}", activity);
            log.warn("exception in #add(activity={})", activity, throwables);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.debug("unable to close a resource {}", rs, ex);
            }
        }
    }

    /**
     * Returns list of all activities in db.
     * @param con connection to db.
     * @throws DAOException only if some exceptions caught while executing statement/getting result set or closing these resources.
     */
    @Override
    public List<Activity> getAll(Connection con) throws DAOException {
        List<Activity> actvts = new ArrayList<>();
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_ACTIVITIES))
        {
            while (rs.next()) {
                Activity ac = getActivity(rs);
                actvts.add(ac);
            }
        } catch (SQLException throwables) {
            log.debug("unable to get all activities", throwables);
            log.warn("exception in #getAll()", throwables);
            throw new DAOException(throwables);
        }
        return actvts;
    }

    /**
     * Get activity by its id.
     * @param con connection to db.
     * @param id activity's id
     * @return activity if there is such in db; null if otherwise.
     * @throws DAOException if an error occurred on connection/statement/resultset lifecycle.
     */
    @Override
    public Activity get(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ACTIVITY_BY_ID)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return getActivity(rs);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #get(id={})", id);
            throw new DAOException(throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException e) {
                log.warn("unable to close a result set {}", rs);
            }
        }
        log.debug("unable to find activity with id={}", id);
        return null;
    }

    /**
     * Updates activity corresponding to given object. Does not update the id field.
     * @param con connection to db
     * @param activity activity to be updated
     * @throws DAOException if caught some exceptions on connection/statement lifecycle.
     */
    @Override
    public void update(Connection con, Activity activity) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(UPDATE_ACTIVITY_BY_ID)) {
            int k = 0;
            ps.setString(++k, activity.getName());
            ps.setString(++k, activity.getDescription());
            ps.setInt(++k, activity.getCategory().getId());
            ps.setInt(++k, activity.getId());
            if (ps.executeUpdate() > 0) {
                log.debug("updated an activity {}", activity);
            } else {
                log.debug("unable to update an activity {}", activity);
            }
        } catch (SQLException throwables) {
            log.warn("exception in #update(activity={})", activity, throwables);
            throw new DAOException(throwables);
        }
    }

    /**
     * Removes an activity by given id.
     * @param id id of activity to be removed.
     * @throws DAOException if activity can not be removed by any reason.
     */
    @Override
    public void remove(Connection con, int id) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_ACTIVITY_BY_ID)) {
            ps.setInt(1, id);

            if (ps.executeUpdate() > 0) {
                log.debug("deleted an activity #id={}", id);
            } else {
                log.debug("unable to delete an activity #id={}", id);
            }
        } catch (SQLException throwables) {
            log.error("error in #remove(id={})", id, throwables);
            throw new DAOException(throwables);
        }
    }

    private static Activity getActivity(ResultSet rs) throws SQLException {
        Activity ac = new Activity();
        ac.setId(rs.getInt(Fields.ACTIVITY_ID));
        ac.setName(rs.getString(Fields.ACTIVITY_NAME));
        ac.setDescription(rs.getString(Fields.ACTIVITY_DESCRIPTION));
        ac.setUsersCount(rs.getInt(Fields.ACTIVITY_USERS_COUNT));
        Category c = new Category(rs.getInt(Fields.ACTIVITY_CATEGORY_ID), rs.getString(Fields.CATEGORY_NAME));
        ac.setCategory(c);
        log.debug("retrieved an activity by name: {}", ac);
        return ac;
    }

    protected static UserActivity getUserActivity(ResultSet rs) throws SQLException {
        Activity ac = getActivity(rs);
        UserActivity ua = new UserActivity(ac);
        ua.setUserId(rs.getInt(Fields.USER_ACTIVITY_USER_ID));
        ua.setAccepted(rs.getBoolean(Fields.USER_ACTIVITY_ACCEPTED));
        ua.setMinutesSpent(rs.getInt(Fields.USER_ACTIVITY_MINUTES_SPENT));
        ua.setRequestedAbandon(rs.getBoolean(Fields.USER_ACTIVITY_REQUESTED_ABANDON));
        log.debug("retrieved a user activity: {}", ua);
        return ua;
    }
}
