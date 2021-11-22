package com.prusan.finalproject.web.command;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * A command for denying user requests for accepting or abandoning an activity.
 */
public class DenyRequestCommand implements Command {
    private static final Logger log = LogManager.getLogger(DenyRequestCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        try {
            UserActivity ua = as.getUserActivity(userId, activityId);
            if (ua == null) {
                log.debug("no such user activity with userId={}, activityId={}", userId, activityId);
                req.setAttribute("err_msg", "no such user activity");
                return new Chain(Pages.ERROR_JSP, true);
            }
            log.debug("retrieved a user activity {}", ua);

            if (!ua.isAccepted()) {
                as.deleteUserActivity(userId, activityId);
                log.debug("denied adding a user activity {}, such row is deleted", ua);
            } else {
                ua.setRequestedAbandon(false);
                log.debug("denied abandonment for a user activity {}", ua);
            }

            removeProcessedRequest(req, userId, activityId, log);

            return new Chain(Pages.REQUESTS_JSP, false);
        } catch (ServiceException e) {
            log.error("unable to download corresponding user activity: userId={}, activityId={}", userId, activityId);
            req.setAttribute("err_msg", "can not download corresponding user activity");
            return new Chain(Pages.ERROR_JSP, true);
        }
    }


    static void removeProcessedRequest(HttpServletRequest req, int userId, int activityId, Logger log) {
        Map<UserActivity, User> requests = (Map<UserActivity, User>) req.getSession().getAttribute("requests");
        UserActivity activityToRemove = new UserActivity();
        activityToRemove.setActivityId(activityId);
        activityToRemove.setUserId(userId);
        User u = requests.remove(activityToRemove);
        if (u != null) {
            log.debug("removed a user activity from the map, userId={}, activityId={}", userId, activityId);
        } else {
            log.debug("could not remove a user activity from the map, userId={}, activityId={}", userId, activityId);
        }
    }
}
