package com.prusan.finalproject.db.service.exception;

import com.prusan.finalproject.db.dao.DAOException;

/**
 * Exception thrown when user tries to insert a duplicate row to the m2m table.
 */
public class DependencyAlreadyExistsException extends ServiceException {
    public DependencyAlreadyExistsException(String s, DAOException e) {
        super(s, e);
    }
}
