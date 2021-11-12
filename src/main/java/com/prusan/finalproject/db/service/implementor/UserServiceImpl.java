package com.prusan.finalproject.db.service.implementor;

import com.prusan.finalproject.db.dao.DAOException;
import com.prusan.finalproject.db.dao.UserDAO;
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

/**
 * UserService interface implementor. Uses DBUtils class for retrieving connections.
 */
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    private final DBUtils dbUtils = DBUtils.getInstance();

    private UserDAO userDAO;

    public void setUserDAO(UserDAO dao) {
        userDAO = dao;
        log.debug("got set userDAO field: {}", dao);
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
        } catch (DAOException e) {
            log.debug("unable to add given user {} ", user, e);
            throw new LoginIsTakenException(user.getLogin());
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
        } catch (DAOException e) {
            log.warn("unable to get user by login '{}' and pass", login, e);
            throw new ServiceException(e);
        }
        if (u != null) {
            return u;
        }
        throw new IncorrectCredentialsException(login);
    }

    @Override
    public void delete(int id) throws ServiceException {
        try (Connection con = dbUtils.getConnection()) {
            userDAO.remove(con, id);
        } catch (SQLException throwables) {
            log.error("unable to get the connection", throwables);
        } catch (DAOException e) {
            log.debug("unable to delete the user by id={}", id, e);
            throw new ServiceException(e);
        }
    }
}
