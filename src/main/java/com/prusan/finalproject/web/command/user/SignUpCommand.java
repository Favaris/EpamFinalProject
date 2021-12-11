package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.web.Encryptor;
import com.prusan.finalproject.web.command.Command;
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
import java.util.ArrayList;
import java.util.List;

public class SignUpCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Validator validator = Validator.getInstance();


    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");

        HttpSession s = req.getSession();

        if (!doValidation(s, login, password, name, surname)) {
            s.setAttribute("invalidUser", User.createDefaultUserWithoutId(login, password, name, surname));
            return Chain.createForward(Pages.SIGN_UP_JSP);
        }

        String hashedPass = Encryptor.encrypt(password);

        User u = User.createDefaultUserWithoutId(login, hashedPass, name, surname);
        log.debug("created a user instance: {}", u);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            us.save(u);
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

    public static boolean doValidation(HttpSession s, String login, String password, String name, String surname) {
        boolean isValid = true;
        List<String> invalidFields = new ArrayList<>();
        if (!validator.validate(Validator.USER_LOGIN, login)) {
            invalidFields.add("login");
            isValid = false;
        }

        if (!validator.validate(Validator.USER_PASSWORD, password)) {
            invalidFields.add("password");
            isValid = false;
        }

        if (!name.isEmpty() && !validator.validate(Validator.USER_NAME, name)) {
            invalidFields.add("name");
            isValid = false;
        }

        if (!surname.isEmpty() && !validator.validate(Validator.USER_SURNAME, surname)) {
            invalidFields.add("surname");
            isValid = false;
        }

        if (!isValid) {
            String fields = String.join(", ", invalidFields.toArray(new String[] {}));
            s.setAttribute("invalidFields", fields);
            log.debug("set up a session attribute 'invalidFields': '{}'", fields);
        }

        return isValid;
    }
}
