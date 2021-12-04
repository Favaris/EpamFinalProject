package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserActivityDAO;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserActivityServiceImpl implements UserActivityService {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private final DBUtils dbUtils = DBUtils.getInstance();
    private UserActivityDAO userActivityDAO;
    public void setUserActivityDAO(UserActivityDAO userActivityDAO) {
        this.userActivityDAO = userActivityDAO;
    }

    @Override
    public void save(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.add(con, ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to add UserActivity: {}", ua, e);
            throw new DependencyAlreadyExistsException("this user activity already exists: " + ua, e);
        }
    }

    /**
     * Gets a List of all users' activities that are not accepted or requested for abandon.
     */
    @Override
    public List<UserActivity> getUsersRequests() throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userActivityDAO.getRequestedUserActivities(con);
            log.debug("retrieved a list with users requests with size={}", list.size());
            return list;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all users' requests", e);
            throw new ServiceException("unable to get all users' requests", e);
        }
    }

    /**
     * Gets a list of all user's activities that are accepted and not requested for abandonment.
     * @throws ServiceException if some connection error occurs.
     */
    @Override
    public List<UserActivity> getAllAcceptedForUser(int userId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userActivityDAO.getAcceptedByUserId(con, userId);
            log.debug("got a list of running activities, list size: {}", list.size());

            return list;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all running activities", e);
            throw new ServiceException("error while trying to load all running activities", e);
        }
    }

    @Override
    public List<UserActivity> getAcceptedForUser(int userId, int start, int end, String orderBy, String[] filterBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<UserActivity> list = userActivityDAO.getAcceptedByUserId(con, userId, end, start, orderBy, filterBy);
            log.debug("received a list of user activities for user #{} with start={}, end={}, orderBy={}, filterBy={}", userId, start, end, orderBy, Arrays.toString(filterBy));
            return list;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get user activities by userId={}, start={}, end={}", userId, start, end, e);
            throw new ServiceException("Failed to download user's activities", e);
        }
    }

    @Override
    public int getActivitiesCountForUser(int userId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = userActivityDAO.getCountByUserId(con, userId);
            log.debug("received amount of user's activities: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("failed to receive a count of user's activities by userId={}", userId, e);
            throw new ServiceException("Failed to get amount of user's activities", e);
        }
    }

    @Override
    public int getSummarizedSpentTimeForUser(int userId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int sum = userActivityDAO.getSummarizedSpentTimeByUserId(con, userId);
            log.debug("received a sum of time spent by user with id={}, sum={}", userId, sum);
            return sum;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("failed to get sum of time spent by user id ={}", userId, e);
            throw new ServiceException("Failed to get a sum of time spent by user on activities", e);
        }
    }

    @Override
    public UserActivity get(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            UserActivity ua = userActivityDAO.get(con, userId, activityId);
            if (ua == null) {
                log.debug("no such user activity, userId={}, activityId={}", userId, activityId);
                throw new NoSuchActivityException("This activity does not exist");
            }
            log.debug("retrieved a user activity: {}", ua);
            return ua;
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get UserActivity by userId={}, activityId={}", userId, activityId, e);
            throw new ServiceException(e);
        }
    }

    /**
     * Updates given user activity.
     * @throws ServiceException if there are connection issues with the db.
     */
    @Override
    public void update(UserActivity ua) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.update(con, ua);
            log.debug("updated a user activity {}", ua);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in updateUserActivity({})", ua, e);
            throw new ServiceException("unable to delete user activity", e);
        }
    }

    @Override
    public void delete(int userId, int activityId) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userActivityDAO.remove(con, userId, activityId);
        } catch (SQLException throwables) {
            log.error("unable to get connection", throwables);
            throw new ServiceException("unable to get connection", throwables);
        } catch (DAOException e) {
            log.error("error in updateUserActivity(userId={}, activityId={})", userId, activityId, e);
            throw new ServiceException("unable to delete user activity", e);
        }
    }
}
