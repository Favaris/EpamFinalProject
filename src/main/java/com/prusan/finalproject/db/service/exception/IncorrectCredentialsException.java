package com.prusan.finalproject.db.service.exception;

public class IncorrectCredentialsException extends ServiceException {
    private String login;

    public IncorrectCredentialsException(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    @Override
    public String getMessage() {
        return "This combination of login and password does not match any registered user. Please, try once more or go to sign up page.";
    }
}
