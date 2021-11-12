package com.prusan.finalproject.db.service.exception;

/**
 * Custom exception for cases when the user we are looking for does not exist. Has the field with the id of non-existing user.
 */
public class NoSuchUserException extends ServiceException {
    private int id;
    public NoSuchUserException (int id) {
        this.id = id;
    }
}
