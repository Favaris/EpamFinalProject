package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserActivityDAO;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.util.Fields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserActivityDAOImpl extends UserActivityDAO {

    private static final Logger log = LogManager.getLogger(UserActivityDAO.class);
    public static final String GET_ALL_ACCEPTED_ACTIVITIES_BY_ID = "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_accepted = 1 AND ua_requested_abandon = 0 AND a_id = ua_activity_id AND c_id = a_category_id";
    public static final String INSERT_USER_ACTIVITY = "INSERT INTO users_m2m_activities VALUES (?, ?, ?, ?, ?)";
    public static final String GET_REQUESTED_USERS_ACTIVITIES =
            "SELECT * FROM users_m2m_activities, activities, categories WHERE (ua_accepted = 0 OR ua_requested_abandon = 1) AND a_id = ua_activity_id AND c_id = a_category_id";
    public static final String UPDATE_USER_ACTIVITY = "UPDATE users_m2m_activities SET ua_accepted = ?, ua_minutes_spent = ?, ua_requested_abandon = ? WHERE ua_activity_id = ? AND ua_user_id = ?";
    public static final String DELETE_USER_ACTIVITY = "DELETE FROM users_m2m_activities WHERE ua_user_id = ? AND ua_activity_id = ?";
    public static final String GET_USER_ACTIVITY =
            "SELECT * FROM users_m2m_activities, activities, categories WHERE ua_user_id = ? AND ua_activity_id = ? AND ua_activity_id = a_id AND a_category_id = c_id";
    public static final String DELETE_ALL_BY_USER_ID = "DELETE FROM users_m2m_activities WHERE ua_user_id = ?";


    @Override
    public void add(Connection con, UserActivity ua) throws DAOException {
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
    public UserActivity get(Connection con, int userId, int activityId) throws DAOException {
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
    public void update(Connection con, UserActivity ua) throws DAOException {
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
    public void remove(Connection con, int userId, int activityId) throws DAOException {
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
     * Retrieves a list of all user's activities that are accepted and not requested for abandonment.
     */
    @Override
    public List<UserActivity> getAcceptedByUserId(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(GET_ALL_ACCEPTED_ACTIVITIES_BY_ID)) {
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

    @Override
    public void removeAllByUserId(Connection con, int userId) throws DAOException {
        try (PreparedStatement ps = con.prepareStatement(DELETE_ALL_BY_USER_ID)){
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

    private Activity getActivity(ResultSet rs) throws SQLException {
        Activity ac = new Activity();
        ac.setName(rs.getString(Fields.ACTIVITY_NAME));
        ac.setId(rs.getInt(Fields.ACTIVITY_ID));
        ac.setUsersCount(rs.getInt(Fields.ACTIVITY_USERS_COUNT));
        ac.setDescription(rs.getString(Fields.ACTIVITY_DESCRIPTION));
        Category c = new Category(rs.getInt(Fields.ACTIVITY_CATEGORY_ID), rs.getString(Fields.CATEGORY_NAME));
        ac.setCategory(c);
        log.debug("retrieved an activity by name: {}", ac);
        return ac;
    }

    private UserActivity getUserActivity(ResultSet rs) throws SQLException {
        Activity ac = getActivity(rs);
        UserActivity ua = new UserActivity(ac);
        ua.setUserId(rs.getInt(Fields.USER_ACTIVITY_USER_ID));
        ua.setMinutesSpent(rs.getInt(Fields.USER_ACTIVITY_MINUTES_SPENT));
        ua.setAccepted(rs.getBoolean(Fields.USER_ACTIVITY_ACCEPTED));
        ua.setRequestedAbandon(rs.getBoolean(Fields.USER_ACTIVITY_REQUESTED_ABANDON));
        log.debug("retrieved a user activity: {}", ua);
        return ua;
    }
}
