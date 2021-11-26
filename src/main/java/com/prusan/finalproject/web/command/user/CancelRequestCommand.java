package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadUsersRequestsCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Cancels a user's request for acceptance or abandonment. Retrieves a user id as parameter 'uId' and an activity id as a parameter 'aId'.
 */
public class CancelRequestCommand implements Command {
    private static final Logger log = LogManager.getLogger(CancelRequestCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();

        try {
            UserActivity ua = as.getUserActivity(userId, activityId);
            log.debug("retrieved a user activity {}", ua);
            if (ua.isRequestedAbandon()) {
                ua.setRequestedAbandon(false);
                as.updateUserActivity(ua);
                log.debug("removed user activity {} from requested for abandonment", ua);
            } else {
                as.deleteUserActivity(userId, activityId);
                log.debug("removed user activity {} from requested for acceptance", ua);
            }

            return new DownloadUsersRequestsCommand().execute(req, resp);
        } catch (NoSuchActivityException ex) {
            log.debug("unable to find a user activity by userId={} and activityId={}", userId, activityId);
            req.getSession().setAttribute("err_msg", ex.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.debug("an error occurred when trying to download and change user activity by userId={} and activityId={}", userId, activityId);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
