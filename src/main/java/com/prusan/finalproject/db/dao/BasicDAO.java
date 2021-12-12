package com.prusan.finalproject.db.dao;

import java.sql.Connection;
import java.util.List;

/**
 * An abstract class for the DAO pattern implementations.
 * @param <T> type on which DAO implementor works.
 */
public interface BasicDAO<T> {

    void add(Connection con, T t) throws DAOException;

    T get(Connection con, int id) throws DAOException;

    void update(Connection con, T t) throws DAOException;

    void remove(Connection con, int id) throws DAOException;

    /**
     * Default method for convenience in DAO implementors. Used to close Connection, Statement, ResultSet objects.
     * @param ac Connection, Statement or ResultSet objects.
     * @throws DAOException wrapped exception thrown when there were some troubles while closing.
     */
    default void close(AutoCloseable ac) throws DAOException {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception ex) {
                throw new DAOException(ex);
            }
        }
    }
}
