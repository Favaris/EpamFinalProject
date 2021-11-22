package com.prusan.finalproject.db.service.exception;

import com.prusan.finalproject.db.dao.DAOException;

/**
 * Exception for cases when a user tries to get access to the activity that does not exists.
 */
public class NoSuchActivityException extends ServiceException {
    public NoSuchActivityException(String s, DAOException e) {
        super(s, e);
    }

    public NoSuchActivityException(String s) {
        super(s);
    }
}
