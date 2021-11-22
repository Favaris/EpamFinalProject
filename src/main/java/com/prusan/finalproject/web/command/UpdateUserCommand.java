package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UpdateUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(UpdateUserCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("id"));
        log.debug("got a user id: {}", userId);
        String name = req.getParameter("name");
        log.debug("got a user name: {}", name);
        String surname = req.getParameter("surname");
        log.debug("got a user surname: {}", surname);

        UserService us = ServiceFactory.getInstance().getUserService();
        try {
            User user = us.getById(userId);
            log.debug("got a user to edit: {}", user);
            user.setName(name);
            user.setSurname(surname);
            log.debug("edited a user: {}", user);
            us.update(user);
            log.debug("updated a user {}", user);
            HttpSession s = req.getSession();
            s.removeAttribute("userToEdit");
            s.removeAttribute("userToEditActivities");
            log.debug("removed edited user specific attributes from the session");
            return new DownloadAllUsersCommand().execute(req, resp);
        } catch (ServiceException e) {
            log.error("error while trying to update a user by id={}", userId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
