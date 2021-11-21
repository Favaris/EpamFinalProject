package com.prusan.finalproject.web.command;

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

            return new DownloadUsersActivitiesCommand().execute(req, resp);
        } catch (ServiceException e) {
            log.error("unable to download corresponding user activity: userId={}, activityId={}", userId, activityId);
            req.setAttribute("err_msg", "can not download corresponding user activity");
            return new Chain(Pages.ERROR_JSP, true);
        }
    }
}
