package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.Validator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUpCommand implements Command {
    private static final Logger log = LogManager.getLogger(SignUpCommand.class);
    private static final Validator validator = Validator.getInstance();
    public static final String LOGIN_ERROR_MESSAGE = "loginErrorMessage";
    public static final String PASSWORD_ERROR_MESSAGE = "passwordErrorMessage";
    public static final String NAME_ERROR_MESSAGE = "nameErrorMessage";
    public static final String SURNAME_ERROR_MESSAGE = "surnameErrorMessage";

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        if (!doValidation(req, login, password, name, surname)) {
            return new Chain("jsp/guest/signUp.jsp", true);
        }

        User u = new User(login, password, name, surname);
        log.debug("created a user instance: {}", u);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            us.save(u);
            HttpSession s = req.getSession();
            s.setAttribute("user", u);
            return new Chain("jsp/user/userPage.jsp", false);
        } catch (LoginIsTakenException ex) {
            log.debug("unable to create new user: login {} ", ex.getLogin());
            req.setAttribute("err_msg", "This login is already taken. Try another one.");
        } catch (ServiceException e) {
            log.debug("unable to add a user {}", u, e);
        }
        return new Chain("jsp/guest/signUp.jsp", true);
    }

    private boolean doValidation(HttpServletRequest req, String login, String password, String name, String surname) {
        boolean isValid = true;
        if (!validator.validate(Validator.USER_LOGIN, login)) {
            req.setAttribute(LOGIN_ERROR_MESSAGE, "true");
            isValid = false;
        } else {
            req.removeAttribute(LOGIN_ERROR_MESSAGE);
        }
        if (!validator.validate(Validator.USER_PASSWORD, password)) {
            req.setAttribute(PASSWORD_ERROR_MESSAGE, "true");
            isValid = false;
        } else {
            req.removeAttribute(PASSWORD_ERROR_MESSAGE);
        }
        if (!name.isEmpty() && !validator.validate(Validator.USER_NAME, name)) {
            req.setAttribute(NAME_ERROR_MESSAGE, "true");
            isValid = false;
        } else {
            req.removeAttribute(NAME_ERROR_MESSAGE);
        }
        if (!surname.isEmpty() && !validator.validate(Validator.USER_SURNAME, surname)) {
            req.setAttribute(SURNAME_ERROR_MESSAGE, "true");
            isValid = false;
        } else {
            req.removeAttribute(SURNAME_ERROR_MESSAGE);
        }
        return isValid;
    }
}
