package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.Encryptor;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.CommandContainer;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        log.debug("retrieved a login: {}", login);
        String password = req.getParameter("password");
        log.debug("retrieved a password");
        String name = req.getParameter("name");
        log.debug("retrieved a name: {}", name);
        String surname = req.getParameter("surname");
        log.debug("retrieved a surname: {}", surname);
        String role = req.getParameter("role");
        log.debug("retrieved a role: {}", role);

        User u = User.createUserWithoutId(login, password, name, surname, role);

        HttpSession session = req.getSession();

        if (!SignUpCommand.doValidation(session, login, password, name, surname)) {
            session.setAttribute("invalidUser", u);
            log.debug("set a session attribute 'invalidUser': {}", u);
            return Chain.createRedirect(Pages.SIGN_UP_JSP);
        }

        String hashedPass = Encryptor.encrypt(password);
        u.setPassword(hashedPass);

        UserService us = ServiceFactory.getInstance().getUserService();

        try {
            us.add(u);
            log.debug("successfully created a new user: {}", u);
            return Chain.createRedirect(String.format("controller?command=%s", CommandContainer.CommandNames.SHOW_ALL_USERS));
        } catch (LoginIsTakenException e) {
            log.debug("login '{}' is already taken", login, e);
            u.setPassword(password);
            session.setAttribute("invalidUser", u);
            log.debug("set a session attribute 'invalidUser': {}", u);
            session.setAttribute("err_msg", "This login is already taken. Try another one.");
            return Chain.createRedirect(Pages.USER_ADD_PAGE);
        } catch (ServiceException e) {
            log.error("failed to create a user: {}", u, e);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
