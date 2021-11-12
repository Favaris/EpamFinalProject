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

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        if (!doValidation(req, login, password, name, surname)) {
            return new Chain("jsp/sign_up.jsp", true);
        }

        User u = new User(login, password, name, surname);
        log.debug("created a user instance: {}", u);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            us.save(u);
            HttpSession s = req.getSession();
            s.setAttribute("user", u);
            return new Chain("jsp/user_page.jsp", false);
        } catch (LoginIsTakenException ex) {
            log.debug("unable to create new user: login {} ", ex.getLogin());
            req.setAttribute("err_msg", "This login is already taken. Try another one.");
        } catch (ServiceException e) {
            log.debug("unable to add a user {}", u, e);
        }
        return new Chain("jsp/sign_up.jsp", true);
    }

    private boolean doValidation(HttpServletRequest req, String login, String password, String name, String surname) {
        boolean isValid = true;
        if (!validator.validate(Validator.USER_LOGIN, login)) {
            req.setAttribute("loginErrorMessage", "Login should be at least 4 characters long and contain only latin characters.");
            isValid = false;
        }
        if (!validator.validate(Validator.USER_PASSWORD, password)) {
            req.setAttribute("passwordErrorMessage", "Password should be at least 4 characters long.");
            isValid = false;
        }
        if (!validator.validate(Validator.USER_NAME, name)) {
            req.setAttribute("nameErrorMessage", "Name and surname should be in latin or cyrillic with length 2-30.");
            isValid = false;
        }
        if (!validator.validate(Validator.USER_SURNAME, surname)) {
            req.setAttribute("surnameErrorMessage", "Name and surname should be in latin or cyrillic with length 2-30.");
            isValid = false;
        }
        return isValid;
    }
}
