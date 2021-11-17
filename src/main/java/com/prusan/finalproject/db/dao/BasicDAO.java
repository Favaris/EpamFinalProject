package com.prusan.finalproject.db.dao;

import java.sql.Connection;
import java.util.List;

/**
 * An abstract class for the DAO pattern implementations.
 * @param <T> type on which DAO implementor works.
 */
public abstract class BasicDAO<T> {
    public abstract void add(Connection con, T t) throws DAOException;

    public abstract List<T> getAll(Connection con) throws DAOException;

    public abstract T get(Connection con, int id) throws DAOException;

    public abstract void update(Connection con, T t) throws DAOException;

    public abstract void remove(Connection con, int id) throws DAOException;

    /**
     * Default method for convenience in DAO implementors. Used to close Connection, Statement, ResultSet objects.
     * @param closeables Connection, Statement or ResultSet objects.
     * @throws DAOException all suppressed exceptions that were caught.
     */
    protected static void close(AutoCloseable... closeables) throws DAOException {
        DAOException e = null;
        for (AutoCloseable ac : closeables) {
            if (ac != null) {
                try {
                    ac.close();
                } catch (Exception ex) {
                    if (e == null) {
                        e = new DAOException(ex);
                    }
                    e.addSuppressed(ex);
                }
            }
        }
        if (e != null) {
            throw e;
        }
    }
}
