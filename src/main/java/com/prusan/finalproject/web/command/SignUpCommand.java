package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.LoginIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SignUpCommand implements Command {
    private static final Logger log = LogManager.getLogger(SignUpCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        User u = new User();
        u.setLogin(req.getParameter("login"));
        u.setPassword(req.getParameter("password"));
        u.setName(req.getParameter("name"));
        u.setSurname(req.getParameter("surname"));
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
}
