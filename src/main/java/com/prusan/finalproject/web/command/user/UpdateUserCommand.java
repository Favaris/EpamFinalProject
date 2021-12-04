package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadAllUsersCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UpdateUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("got a user id: {}", userId);
        String name = req.getParameter("name");
        log.debug("got a user name: {}", name);
        String surname = req.getParameter("surname");
        log.debug("got a user surname: {}", surname);


        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();

        try {
            User user = us.getById(userId);
            log.debug("got a user to edit: {}", user);
            user.setName(name);
            user.setSurname(surname);
            us.update(user);
            log.debug("edited a user: {}", user);
            log.debug("successfully saved all changes");

            return new Chain("controller?command=showDetailedUserInfo&uId="+userId, false);
        } catch (ServiceException e) {
            log.error("error while trying to update a user by id={}", userId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
