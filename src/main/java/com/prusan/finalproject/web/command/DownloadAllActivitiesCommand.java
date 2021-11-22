package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Intermediate command for setting a list of all categories as the request attribute 'activities'. If user role is 'admin', just loads all activities; if the role is 'user', loads only activities that are not already taken by this user.
 */
public class DownloadAllActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        ActivityService as = ServiceFactory.getInstance().getActivityService();
        User u = (User) req.getSession().getAttribute("user");

        try {
            List<Activity> activities;
            if ("admin".equals(u.getRole())) {
                activities = as.getAll();
            } else {
                activities = as.getAllActivitiesNotTakenByUser(u.getId());
            }
            req.getSession().setAttribute("activities", activities);
            log.debug("set activities list as the request attribute");
        } catch (ServiceException e) {
            log.error("can not get all activities", e);
            req.setAttribute("err_msg", "can not get all activities");
            return new Chain(Pages.ERROR_JSP, true);
        }

        //return (Chain) req.getAttribute("nextChain");
        return new Chain(Pages.ACTIVITIES_JSP, false);
    }
}
