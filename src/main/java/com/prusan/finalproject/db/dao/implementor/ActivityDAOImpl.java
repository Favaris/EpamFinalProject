package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.UserActivity;
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
    public static final String GET_ACTIVITY_BY_NAME = "SELECT * FROM activities a WHERE a.name = ?";
    public static final String INSERT_ACTIVITY = "INSERT INTO activities(name, description) VALUES (?,?)";
    public static final String GET_ACTIVITY_BY_ID = "SELECT * FROM activities WHERE id = ?";
    public static final String UPDATE_ACTIVITY_BY_ID = "UPDATE activities a SET name = ?, description = ? WHERE a.id = ?";
    public static final String DELETE_ACTIVITY_BY_ID = "DELETE FROM activities WHERE id = ?";
    public static final String INSERT_CATEGORY_BY_IDS = "INSERT INTO categories_m2m_activities VALUES (?, ?)";
    public static final String GET_ALL_CATEGORIES_IDS = "SELECT category_id FROM categories_m2m_activities WHERE activity_id = ?";
    public static final String INSERT_USER_ACTIVITY = "INSERT INTO users_m2m_activities VALUES (?, ?, ?, ?, ?)";
    public static final String GET_REQUESTED_USERS_ACTIVITIES = "SELECT * FROM users_m2m_activities ua, activities a WHERE (ua.accepted = 0 OR ua.requested_abandon = 1) AND a.id = ua.activity_id";
    public static final String UPDATE_USER_ACTIVITY = "UPDATE users_m2m_activities ua SET accepted = ?, minutes_spent = ?, requested_abandon = ? WHERE activity_id = ? AND user_id = ?";
    public static final String DELETE_USER_ACTIVITY = "DELETE FROM users_m2m_activities WHERE user_id = ? AND activity_id = ?";
    public static final String GET_USER_ACTIVITY = "SELECT * FROM users_m2m_activities ua, activities a WHERE ua.user_id = ? AND ua.activity_id = ? AND ua.activity_id = a.id";
    public static final String GET_ACTIVITIES_BY_USER_ID = "SELECT a.id, a.description, a.name FROM activities a, users_m2m_activities ua WHERE ua.activity_id = a.id AND ua.user_id = ?";
    public static final String DELETE_ALL_ACTIVITY_CATEGORIES = "DELETE FROM categories_m2m_activities WHERE activity_id = ?";


    @Override
    public Activity getByName(Connection con, String name) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ACTIVITY_BY_NAME)) {
            ps.setString(1, name);
            rs = ps.executeQuery();
            if (rs.next()) {
                Activity ac = new Activity();
                ac.setId(rs.getInt("id"));
                ac.setName(rs.getString("name"));
                ac.setDescription(rs.getString("description"));
                log.debug("retrieved an activity by name: {}", ac);
                return ac;
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
     * Adds new dependence between category and activity.
     * @throws DAOException thrown if this dependence already exists.
     */
    @Override
    public void addCategory(Connection con, int categoryId, int activityId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(INSERT_CATEGORY_BY_IDS)) {
            int k = 0;
            ps.setInt(++k, categoryId);
            ps.setInt(++k, activityId);

            ps.executeUpdate();
        } catch (SQLException throwables) {
            log.debug("unable to insert category_activity dependence: catId={}, actId={}", categoryId, activityId, throwables);
            throw new DAOException("dependence already exists", throwables);
        }
    }

    /**
     * Returns a list of all categories ids of the given activity.
     * @param id activity id
     * @throws DAOException thrown if there is no activity with the given id.
     */
    @Override
    public List<Integer> getCategoriesIds(Connection con, int id) throws DAOException {
        List<Integer> categories = new ArrayList<>();
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ALL_CATEGORIES_IDS)) {
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                categories.add(rs.getInt(1));
            }
            return categories;
        } catch (SQLException throwables) {
            log.debug("no such activity with id: #{}", id, throwables);
            log.error("exception caught in #getAllCategories(id={})", id, throwables);
            throw new DAOException("no such activity with id: " + id, throwables);
        } finally {
            try {
                close(rs);
            } catch (DAOException ex) {
                log.warn("unable to close resource {}", rs, ex);
            }
        }
    }

    /**
     * Deletes all dependencies in categories_m2m_activities table that have the id of activity.
     * @param activityId id of activity to delete all categories from
     * @throws DAOException is thrown only if there are some connection issues
     */
    @Override
    public void deleteAllCategories(Connection con, int activityId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_ALL_ACTIVITY_CATEGORIES)) {
            ps.setInt(1, activityId);
            if (ps.executeUpdate() > 0) {
                log.debug("successfully deleted all categories of an activity with id={}", activityId);
            } else {
                log.debug("activity with id={} had no categories to delete", activityId);
            }
        } catch (SQLException throwables) {
            log.error("error in deleteAllCategories(actId={})", activityId, throwables);
            throw new DAOException(throwables);
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
    public List<Activity> getActivitiesByUserId(Connection con, int userId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ACTIVITIES_BY_USER_ID)) {
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            List<Activity> result = new ArrayList<>();
            while (rs.next()) {
                Activity a = getActivity(rs);
                log.debug("retrieved activity by user id={}, activity={}", userId, a);
                result.add(a);
            }
            log.debug("retrieved a list of all activities, specific to a user with id={}, list size: {}", userId, result.size());
            return result;
        } catch (SQLException throwables) {
            log.error("unable to download all activities specific to a user with id={}", userId, throwables);
            throw new DAOException(throwables);
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
             ResultSet rs = st.executeQuery("SELECT * FROM activities"))
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

    private Activity getActivity(ResultSet rs) throws SQLException {
        Activity ac = new Activity();
        ac.setId(rs.getInt("id"));
        ac.setName(rs.getString("name"));
        ac.setDescription(rs.getString("description"));
        log.debug("retrieved an activity: {}", ac);
        return ac;
    }

    protected static UserActivity getUserActivity(ResultSet rs) throws SQLException {
        UserActivity ua = new UserActivity();
        ua.setUserId(rs.getInt("user_id"));
        ua.setActivityId(rs.getInt("activity_id"));
        ua.setAccepted(rs.getBoolean("accepted"));
        ua.setMinutesSpent(rs.getInt("minutes_spent"));
        ua.setId(rs.getInt("id"));
        ua.setName(rs.getString("name"));
        ua.setDescription(rs.getString("description"));
        ua.setRequestedAbandon(rs.getBoolean("requested_abandon"));
        log.debug("retrieved a user activity: {}", ua);
        return ua;
    }
}
