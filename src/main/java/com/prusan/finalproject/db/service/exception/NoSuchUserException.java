package com.prusan.finalproject.db.service.exception;

/**
 * Custom exception for cases when the user we are looking for does not exist. Has the field with the id of non-existing user.
 */
public class NoSuchUserException extends ServiceException {
    public NoSuchUserException(String message) {
        super(message);
    }
}
