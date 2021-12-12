package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.*;
import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Activity Service default implementor. Uses DBUtils.getConnection() connection. For it to work properly, it is needed to set all dao implementor fields on this object.
 */
public class ActivityServiceImpl implements ActivityService {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private final DBUtils dbUtils = DBUtils.getInstance();

    private ActivityDAO activityDAO;

    public void setActivityDAO(ActivityDAO activityDAO) {
        this.activityDAO = activityDAO;
    }

    /**
     * Saves given activity to the db. Also saves all its categories in the corresponding table.
     * @throws NameIsTakenException if this activity already exists.
     * @throws ServiceException if operation was unsuccessful.
     */
    @Override
    public void add(Activity activity) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.add(con, activity);

            log.debug("saved an activity {}", activity);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to save activity {}", activity, e);
            throw new NameIsTakenException("Activity with name '" + activity.getName() + "' already exists. Please, try another name.", e);
        }
    }

    @Override
    public void update(Activity activity) throws ServiceException {
        try (Connection con = dbUtils.getConnection()){
            activityDAO.update(con, activity);

            log.debug("updated info about activity {}", activity);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to update an activity {}", activity, e);
            throw new NameIsTakenException("Activity with name '" + activity.getName() + "' already exists. Please, try another name.", e);
        }
    }

    @Override
    public List<Activity> getActivities(int start, int amount, String orderBy, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Activity> list = activityDAO.getActivities(con, amount, start, orderBy, filterBy);
            log.debug("retrieved a list of activities sorted by '{}', with start={}, amount={}, list size={}", orderBy, start, amount, list.size());

            return list;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get a list of activities sorted by {}, start={}, amount={}", orderBy, start, amount, e);
            throw new ServiceException("Failed to get a list of sorted activities", e);
        }
    }

    @Override
    public int getActivitiesCount(int userId, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count;
            if ("all".equals(filterBy[0])) {
                count = activityDAO.getCount(con, userId);
            } else {
                count = activityDAO.getFilteredCount(con, userId, filterBy);
            }
            log.debug("received a number of activities: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get activities count with filters {}", Arrays.toString(filterBy), e);
            throw new ServiceException("Failed to get activities count", e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            activityDAO.remove(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to delete an activity by id={}", id, e);
            throw new NoSuchActivityException("activity with id="+ id + "does not exist", e);
        }
    }

    @Override
    public List<Activity> getActivitiesNotTakenByUser(int userId, int start, int end, String orderBy, String... filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<Activity> result = activityDAO.getAvailableActivitiesForUserId(con, userId, end, start, orderBy, filterBy);
            log.debug("received a list of all activities that are not taken by this user, list size={}", result.size());
            return result;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error while trying to download all activities and user's activities, userId={}", userId, e);
            throw new ServiceException("Can not download a list of activities", e);
        }
    }

}
