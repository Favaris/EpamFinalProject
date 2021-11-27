package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Intermediate command for setting a list of all categories as the request attribute 'activities'.
 * <pre>
 *   If user role is 'admin', loads all activities if there is no request parameter 'uId'.
 *   If there is a param 'uId' and user is admin, that means that admin wants to add a new activity to a certain user, so the command downloads all activities that are not taken by user with such id;
 *   If the role is 'user', loads only activities that are not already taken by this user.
 * </pre>

 */
public class DownloadAllActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService as = ServiceFactory.getInstance().getActivityService();
        User u = (User) req.getSession().getAttribute("user");
        if (u == null) {
            return new Chain(Pages.SIGN_IN_JSP, false);
        }

        try {
            List<Activity> activities;
            if ("admin".equals(u.getRole())) {
                log.debug("user {} is admin", u);
                if (req.getParameter("uId") != null) {
                    int userId = Integer.parseInt(req.getParameter("uId"));
                    log.debug("retrieved a parameter uId={}", userId);
                    activities = as.getAllActivitiesNotTakenByUser(userId);
                    log.debug("downloaded all activities not taken by a user with id {}, list size={}", userId, activities.size());
                } else {
                    log.debug("did not find a param 'uId'");
                    activities = as.getAll();
                    log.debug("downloaded a list of all activities, list size={}", activities.size());
                }
            } else {
                log.debug("user {} is default user", u);
                activities = as.getAllActivitiesNotTakenByUser(u.getId());
                log.debug("downloaded a list of all activities not taken by a user {}, list size={}", u, activities.size());
            }

            if (req.getAttribute("nextChain") != null) {
                Chain nextChain = (Chain) req.getAttribute("nextChain");
                log.debug("retrieved a 'nextChain' attribute ");
                if (nextChain.isDoForward()) {
                    req.setAttribute("activities", activities);
                    log.debug("set activities list as a request attribute");
                } else {
                    req.getSession().setAttribute("activities", activities);
                    log.debug("set activities list as a session attribute");
                }
                return nextChain;
            }

            req.setAttribute("activities", activities);
            log.debug("set activities list as a request attribute");
            return new Chain(Pages.ACTIVITIES_JSP, true);
        } catch (ServiceException e) {
            log.error("can not get all activities", e);
            req.setAttribute("err_msg", "can not get all activities");
            return new Chain(Pages.ERROR_JSP, true);
        }

    }
}
