package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.ValidationErrorsFlags;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.Validator;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUpCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Validator validator = Validator.getInstance();


    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        if (!doValidation(req, login, password, name, surname)) {
            req.getSession().setAttribute("invalidUser", new User(login, password, name, surname));
            return Chain.createForward(Pages.SIGN_UP_JSP);
        }

        User u = new User(login, password, name, surname);
        log.debug("created a user instance: {}", u);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            us.save(u);
            HttpSession s = req.getSession();
            s.setAttribute("user", u);
            return Chain.createRedirect(Pages.HOME_JSP);
        } catch (LoginIsTakenException ex) {
            log.debug("unable to create new user: login {} ", ex.getLogin());
            HttpSession session = req.getSession();
            session.setAttribute("invalidUser", u);
            session.setAttribute("err_msg", "This login is already taken. Try another one.");
            return Chain.createRedirect(Pages.SIGN_UP_JSP);
        } catch (ServiceException e) {
            log.debug("unable to add a user {}", u, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }

    public static boolean doValidation(HttpServletRequest req, String login, String password, String name, String surname) {
        boolean isValid = true;
        HttpSession session = req.getSession();
        if (!validator.validate(Validator.USER_LOGIN, login)) {
            session.setAttribute(ValidationErrorsFlags.LOGIN_ERROR_MESSAGE, "");
            isValid = false;
        }

        if (!validator.validate(Validator.USER_PASSWORD, password)) {
            session.setAttribute(ValidationErrorsFlags.PASSWORD_ERROR_MESSAGE, "");
            isValid = false;
        }

        if (!name.isEmpty() && !validator.validate(Validator.USER_NAME, name)) {
            session.setAttribute(ValidationErrorsFlags.USER_NAME_ERROR_MESSAGE, "");
            isValid = false;
        }

        if (!surname.isEmpty() && !validator.validate(Validator.USER_SURNAME, surname)) {
            session.setAttribute(ValidationErrorsFlags.USER_SURNAME_ERROR_MESSAGE, "");
            isValid = false;
        }

        return isValid;
    }
}
