package com.prusan.finalproject.db.service.exception;

/**
 * Exception for cases when user tries to create an acc with already taken login. Has the field with that login.
 */
public class LoginIsTakenException extends ServiceException {
    private final String login;

    public LoginIsTakenException(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
