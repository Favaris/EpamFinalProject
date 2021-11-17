package com.prusan.finalproject.db.service.exception;

/**
 * Exception for cases when there is already an activity/category with such name in the db.
 */
public class NameIsTakenException extends ServiceException {
    public NameIsTakenException(String s, Exception e) {
        super(s, e);
    }
}
