package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("received a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("received an activityId={}", activityId);
        List<UserActivity> userActivities = (List<UserActivity>) session.getAttribute("userToEditActivities");
        log.debug("received a list of user's activities: {}", userActivities);

        if (userActivities == null) {
            log.error("failed to find a list of user's activities in session attribute 'userToEditActivities'");
            session.setAttribute("err_msg", "Did not find a list of user's activities");
            return new Chain(Pages.ERROR_JSP, false);
        }

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            Activity a = as.getById(activityId);
            log.debug("received an activity instance: {}", a);
            UserActivity ua = new UserActivity(a);
            ua.setUserId(userId);
            log.debug("created a user activity instance: {}", a);

            if (userActivities.contains(ua)) {
                Set<UserActivity> removedActivities = (Set<UserActivity>) session.getAttribute("removedActivities");
                if (removedActivities == null) {
                    log.error("failed to find a list of user's activities in session attribute 'removedActivities'");
                    session.setAttribute("err_msg", "Did not find a list of user's activities");
                    return new Chain(Pages.ERROR_JSP, false);
                }
                removedActivities.remove(ua);
                log.debug("removed a user activity from the 'removedActivities' set");
                String url = handler.getURLParametersString(req);
                return new Chain(String.format("controller?command=showEditUserPage&uId=%d&", userId) + url, false);
            }

            userActivities.add(ua);
            log.debug("added user activity {} to the 'userToEditActivities' list, list size: {}", ua, userActivities.size());
            String queryString = handler.getQueryStringWithSortingParameters(session);
            return new Chain(String.format("controller?command=showActivitiesPage&uId=%d&", userId) + queryString, false);
        } catch (NoSuchActivityException e) {
            log.warn("did not find an activity by activityId={}", activityId, e);
            session.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("failed to get a user activity by activityId={}", activityId, e);
            session.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
