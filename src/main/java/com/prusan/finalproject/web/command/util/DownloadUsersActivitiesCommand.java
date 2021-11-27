package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
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
 * Intermediate command. Downloads all activities that belong to a certain user by his id.
 */
public class DownloadUsersActivitiesCommand implements Command {
    private final static Logger log = LogManager.getLogger(DownloadUsersActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        User u = (User) req.getSession().getAttribute("user");
        log.debug("retrieved a user from the session: {}", u);

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        try {
            List<UserActivity> list = as.getAllRunningUsersActivities(u.getId());
            log.debug("got a list of running activities, list size: {}", list.size());

            if (req.getAttribute("nextChain") != null) {
                Chain nextChain = (Chain) req.getAttribute("nextChain");
                if (nextChain.isDoForward()) {
                    req.setAttribute("runningActivities", list);
                    log.debug("set 'runningActivities' attribute in the request scope");
                } else {
                    req.getSession().setAttribute("runningActivities", list);
                    log.debug("set 'runningActivities' attribute in the session scope");
                }
                log.debug("retrieved a next Chain {}", nextChain);
                return nextChain;
            }

            req.setAttribute("runningActivities", list);
            return new Chain(Pages.RUNNING_ACTIVITIES_JSP, true);
        } catch (ServiceException e) {
            log.error("error in #execute(), user={}", u, e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, true);
        }
    }
}
