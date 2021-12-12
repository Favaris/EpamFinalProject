package com.prusan.finalproject.db.dao.implementor;

import com.prusan.finalproject.db.dao.ActivityDAO;
import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.util.Fields;
import com.prusan.finalproject.db.util.PaginationQueries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.prusan.finalproject.db.util.SQLQueries.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * a DAO layer for Activity entity.
 */
public class ActivityDAOImpl implements ActivityDAO {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

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

    @Override
    public int getCount(Connection con, int userId) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(ActivityQueries.GET_COUNT_OF_AVAILABLE_ACTIVITIES_FOR_USER)) {
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
        try (PreparedStatement ps = con.prepareStatement(String.format(ActivityQueries.GET_AVAILABLE_ACTIVITIES_COUNT_WITH_FILTERS, filters))) {
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
        try (PreparedStatement ps = con.prepareStatement(ActivityQueries.INSERT_ACTIVITY, Statement.RETURN_GENERATED_KEYS)) {
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
     * Get activity by its id.
     * @param con connection to db.
     * @param id activity's id
     * @return activity if there is such in db; null if otherwise.
     * @throws DAOException if an error occurred on connection/statement/resultset lifecycle.
     */
    @Override
    public Activity get(Connection con, int id) throws DAOException {
        ResultSet rs = null;
        try (PreparedStatement ps = con.prepareStatement(ActivityQueries.GET_ACTIVITY_BY_ID)) {
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
        try (PreparedStatement ps = con.prepareStatement(ActivityQueries.UPDATE_ACTIVITY_BY_ID)) {
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
        try (PreparedStatement ps = con.prepareStatement(ActivityQueries.DELETE_ACTIVITY_BY_ID)) {
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
        Activity.Builder builder = new Activity.Builder();
        Category c = new Category(rs.getInt(Fields.ACTIVITY_CATEGORY_ID), rs.getString(Fields.CATEGORY_NAME));

        Activity ac = builder.
                setId(rs.getInt(Fields.ACTIVITY_ID)).
                setName(rs.getString(Fields.ACTIVITY_NAME)).
                setDescription(rs.getString(Fields.ACTIVITY_DESCRIPTION)).
                setUsersCount(rs.getInt(Fields.ACTIVITY_USERS_COUNT)).
                setCategory(c).
                create();
        log.debug("retrieved an activity instance: {}", ac);
        return ac;
    }
}
