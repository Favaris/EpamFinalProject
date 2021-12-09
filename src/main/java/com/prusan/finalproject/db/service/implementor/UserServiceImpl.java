package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.*;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.exception.IncorrectCredentialsException;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.NoSuchUserException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.util.DBUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * UserService interface implementor. Uses DBUtils class for retrieving connections.
 */
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private final DBUtils dbUtils = DBUtils.getInstance();

    private UserDAO userDAO;
    private UserActivityDAO userActivityDAO;

    public void setUserActivityDAO(UserActivityDAO userActivityDAO) {
        this.userActivityDAO = userActivityDAO;
    }

    public void setUserDAO(UserDAO dao) {
        userDAO = dao;
    }

    /**
     * Saves given user to database. User object gets id field filled if the query was successful.
     * @param user a user to add to the db.
     */
    @Override
    public void save(User user) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.add(con, user);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException(throwables);
        } catch (DAOException e) {
            log.debug("unable to add given user {} ", user, e);
            throw new LoginIsTakenException(user.getLogin());
        }
    }

    @Override
    public void update(User user) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.update(con, user);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException(throwables);
        } catch (DAOException e) {
            log.error("error in #update({})", user, e);
            throw new ServiceException("Can not update a user " + user, e);
        }
    }

    @Override
    public User getById(int id) throws ServiceException {
        User u = null;
        try (Connection con = dbUtils.getConnection()) {
            u = userDAO.get(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.warn("unable to get user with id={}", id, e);
            throw new ServiceException(e);
        }
        if (u != null) {
            return u;
        }
        throw new NoSuchUserException(id);
    }

    @Override
    public User getByLoginAndPass(String login, String pass) throws ServiceException {
        User u = null;
        try (Connection con = dbUtils.getConnection()) {
            u = userDAO.getByLoginAndPassword(con, login, pass);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.warn("unable to get user by login '{}' and pass", login, e);
            throw new ServiceException(e);
        }
        if (u != null) {
            log.debug("returned a user by login and pass {}", u);
            return u;
        }
        throw new IncorrectCredentialsException(login);
    }

    /**
     * Return a list of all users with role='user'.
     * @throws ServiceException if there are some connection issues with the db.
     * @param end
     * @param start
     * @param countLessThen
     * @param countBiggerThen
     */
    @Override
    public List<User> getWithRoleUser(int start, int amount, String orderBy, String countLessThen, String countBiggerThen, String searchBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            List<User> users = userDAO.getWithRoleUser(con, amount, start, orderBy, countLessThen, countBiggerThen, searchBy);
            log.debug("retrieved a list of all users with role='user', list size: {}", users.size());
            return users;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("unable to get all users with role='user'");
            throw new ServiceException("error while downloading all users", e);
        }
    }

    @Override
    public int getDefaultUsersCount(String countLessThen, String countBiggerThen, String searchBy) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            int count = userDAO.getCountWithRoleUser(con, countLessThen, countBiggerThen, searchBy);
            log.debug("received a default users count: {}", count);
            return count;
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.error("failed to get default users count", e);
            throw new ServiceException("Failed to get amount of all default users", e);
        }
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.remove(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
            throw new ServiceException("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to delete the user by id={}", id, e);
            throw new ServiceException(e);
        }
    }
}
