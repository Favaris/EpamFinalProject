package com.prusan.finalproject.db.service.exception;

import com.prusan.finalproject.db.dao.DAOException;

public class DependencyAlreadyExistsException extends ServiceException {
    public DependencyAlreadyExistsException(String s, DAOException e) {
        super(s, e);
    }
}
