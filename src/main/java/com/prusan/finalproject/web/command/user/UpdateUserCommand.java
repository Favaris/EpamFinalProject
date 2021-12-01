package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
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
    private static final Logger log = LogManager.getLogger(UpdateUserCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        int userId = Integer.parseInt(req.getParameter("id"));
        log.debug("got a user id: {}", userId);
        String name = req.getParameter("name");
        log.debug("got a user name: {}", name);
        String surname = req.getParameter("surname");
        log.debug("got a user surname: {}", surname);

        List<UserActivity> userActivities = (List<UserActivity>) session.getAttribute("userToEditActivities");
        log.debug("received a list of user's activities: {}", userActivities);
        if (userActivities == null) {
            log.error("did not find a list of user's activities");
            session.setAttribute("err_msg", "Unable to get a list of user's activities");
            return new Chain(Pages.ERROR_JSP, false);
        }

        Set<UserActivity> removedActivities = (Set<UserActivity>) session.getAttribute("removedActivities");
        log.debug("received a list of removed activities: {}", removedActivities);
        if (removedActivities == null) {
            removedActivities = new HashSet<>();
            log.debug("the list was null, created an empty stub");
        }

        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        ActivityService as = sf.getActivityService();

        try {
            User user = us.getById(userId);
            log.debug("got a user to edit: {}", user);
            user.setName(name);
            user.setSurname(surname);
            log.debug("edited a user: {}", user);
            userActivities = filterOutRemovedActivities(userActivities, removedActivities);
            for (UserActivity ua : userActivities) {
                ua.setAccepted(true);
            }
            us.updateWithUserActivities(user, userActivities);
            log.debug("successfully saved all changes");
            session.removeAttribute("userToEdit");
            session.removeAttribute("removedActivities");
            session.removeAttribute("userToEditActivities");
            log.debug("removed all used attributes from the session");

            return new Chain("controller?command=showAllUsers", false);
        } catch (ServiceException e) {
            log.error("error while trying to update a user by id={}", userId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }

    private List<UserActivity> filterOutRemovedActivities(List<UserActivity> userActivities, Set<UserActivity> finalRemovedActivities) {
        List<UserActivity> list =  userActivities.stream()
                .filter(ua -> !finalRemovedActivities.contains(ua))
                .collect(Collectors.toList());
        log.debug("filtered out all removed user activities, result list size: {}", list.size());
        return list;
    }
}
