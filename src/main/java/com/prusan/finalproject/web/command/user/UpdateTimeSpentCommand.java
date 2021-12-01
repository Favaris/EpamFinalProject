package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadUsersActivitiesCommand;
import com.prusan.finalproject.web.command.util.DownloadUsersRequestsCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This command updates user's time spent on a certain activity by adding new values the old ones.
 * <br> Receives next parameters: <br>
 * <pre>
 *     'uId' - user's Id
 *     'aId' - activity's Id
 *     'hours' - hours to add
 *     'minutes' - minutes to add
 * </pre>
 */
public class UpdateTimeSpentCommand implements Command {
    private static final Logger log = LogManager.getLogger(UpdateTimeSpentCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id ={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);
        int hours = Integer.parseInt(req.getParameter("hours"));
        log.debug("retrieved an hours count={}", hours);
        int minutes = Integer.parseInt(req.getParameter("minutes"));
        log.debug("retrieved a minutes count={}", minutes);

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            UserActivity ua = as.getUserActivity(userId, activityId);
            log.debug("successfully retrieved a user activity {}", ua);
            int additionalMinutes = hours * 60 + minutes;
            ua.setMinutesSpent(ua.getMinutesSpent() + additionalMinutes);
            as.updateUserActivity(ua);
            log.debug("added {} minutes to time count on user activity {}", additionalMinutes, ua);

            return new Chain("controller?command=downloadUsersActivities", false);
        } catch (NoSuchActivityException ex) {
            log.error("no such activity with userId={} and activityId={}", userId, activityId, ex);
            req.getSession().setAttribute("err_msg", ex.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("error while trying to update spent time on user activity with userId={} and activityId={}", userId, activityId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
