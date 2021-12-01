package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadUsersActivitiesCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestActivityAbandonmentCommand implements Command {
    private static final Logger log = LogManager.getLogger(RequestActivityAbandonmentCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        Integer userId = Integer.valueOf(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        Integer activityId = Integer.valueOf(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            UserActivity ua = as.getUserActivity(userId, activityId);
            log.debug("successfully retrieved a user activity instance: {}", ua);
            ua.setRequestedAbandon(true);
            as.updateUserActivity(ua);
            log.debug("successfully updated a user activity {}", ua);
        } catch (NoSuchActivityException ex) {
            log.debug("unable to get a user activity instance with userId={}, activityId={}", userId, activityId, ex);
            req.setAttribute("err_msg", ex.getMessage());
        } catch (ServiceException e) {
            log.debug("unable to get a user activity instance with userId={}, activityId={} or to update it", userId, activityId, e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }

        return new Chain("controller?command=downloadUsersActivities", false);
    }
}
