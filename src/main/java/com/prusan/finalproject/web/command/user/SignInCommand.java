package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.IncorrectCredentialsException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.Encryptor;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * A command for signing in.
 */
public class SignInCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        log.debug("retrieved a login: {}", login);

        if (login == null && password == null) {
            return Chain.createRedirect(Pages.SIGN_IN_JSP);
        }

        String hashedPass = Encryptor.encrypt(password);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            User u = us.getByLoginAndPass(login, hashedPass);
            if (u != null) {
                log.debug("retrieved a user by login '{}' and pass", login);
                HttpSession ses = req.getSession();
                ses.setAttribute("user", u);
                return Chain.createRedirect(Pages.HOME_JSP);
            }
        } catch (IncorrectCredentialsException e) {
            log.debug("incorrect credentials with login: {}", e.getLogin());
            HttpSession session = req.getSession();
            session.setAttribute("invalidLogin", login);
            session.setAttribute("err_msg", e.getMessage());
        } catch (ServiceException e) {
            log.warn("could not get user by login {} and pass properly", login, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        return Chain.createRedirect(Pages.SIGN_IN_JSP);
    }

}
