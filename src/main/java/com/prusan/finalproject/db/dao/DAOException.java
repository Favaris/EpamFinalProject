package com.prusan.finalproject.db.dao;

/**
 * Custom exception wrapper for all exceptions caught in DAO implementors.
 */
public class DAOException extends Exception {
    public DAOException() {
        super();
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
