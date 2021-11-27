package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.NoSuchUserException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Downloads all info about the user by its id. Retrieves this id from request attribute 'id'. Downloads user and his running activities in session attributes as 'userToEdit' and 'userToEditActivities'.
 */
public class PrepareForUserEditingCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForUserEditingCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved user id from request: {}", userId);

        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        ActivityService as = sf.getActivityService();
        HttpSession s = req.getSession();

        try {
            User u = us.getById(userId);
            log.debug("retrieved a user {}", u);
            List<UserActivity> userActivities = as.getAllRunningUsersActivities(userId);
            log.debug("retrieved user's activities list, list size: {}", userActivities.size());
            s.setAttribute("userToEdit", u);
            s.setAttribute("userToEditActivities", userActivities);
            log.debug("set retrieved entities as session attributes");

            return new Chain(Pages.USER_EDIT_PAGE_JSP, false);
        } catch (NoSuchUserException ex) {
            log.debug("no such user with id={}", userId);
            s.setAttribute("err_msg", "User not exists");
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("error while trying to load a user by id={}", userId, e);
            s.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
