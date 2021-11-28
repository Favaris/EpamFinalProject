package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.PrepareForAdditionActivitiesForUserCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds a new user activity by user id and activity id. This activity is already set accepted.
 * <pre>
 *     Retrieves next request params:
 *     'uId' - user id;
 *     'aId' - activity id;
 * </pre>
 */
public class AddUserActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(AddUserActivityCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        UserActivity ua = new UserActivity();
        ua.setUserId(userId);
        ua.setActivityId(activityId);
        ua.setAccepted(true);

        try {
            as.addUserActivity(ua);
            log.debug("successfully added a new user activity {}", ua);
            return new Chain("controller?command=showAddActivityForUserPage&uId=" + userId, false);
        }  catch (DependencyAlreadyExistsException e) {
            log.debug("such user activity already exists {}", ua, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("failed to save a user activity {}", ua, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
