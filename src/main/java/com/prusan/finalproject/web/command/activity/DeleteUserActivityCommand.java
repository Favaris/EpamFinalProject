package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.PrepareForUserEditingCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <pre>
 *  WARN: this command DOES NOT delete a user activity from the db.
 *  It removes corresponding activity from user activities list as session scope attribute 'userToEditActivities'
 *     Retrieves next request parameters:
 *     'uId' - user id
 *     'aId' - activity id
 * </pre>
 */
public class DeleteUserActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(DeleteUserActivityCommand.class);
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);
        List<UserActivity> userActivities = (List<UserActivity>) session.getAttribute("userToEditActivities");
        log.debug("retrieved a list of user's activities: {}", userActivities);

        if (userActivities == null) {
            log.error("failed to find a list of user's activities in session attribute 'userToEditActivities'");
            session.setAttribute("err_msg", "Did not find a list of user's activities");
            return new Chain(Pages.ERROR_JSP, false);
        }

        UserActivity ua = new UserActivity(userId, activityId);
        log.debug("created a user activity stub: {}", ua);
        int index = userActivities.indexOf(ua);
        if (index == -1) {
            log.warn("unable to find a user activity {} in a list {}", ua, userActivities);
            session.setAttribute("err_msg", "This activity has been already removed");
            return new Chain(Pages.ERROR_JSP, false);
        }
        ua = userActivities.get(index);
        log.debug("retrieved a user activity {} from user's activities list", ua);

        Set<UserActivity> removedActivities = (Set<UserActivity>) session.getAttribute("removedActivities");
        if (removedActivities == null) {
            removedActivities = new HashSet<>();
            session.setAttribute("removedActivities", removedActivities);
        }
        removedActivities.add(ua);
        log.debug("added activity {} to the 'removedActivities' set, set size: {}", ua, removedActivities.size());

        String queryString = handler.getQueryString(session);
        return new Chain(String.format("controller?command=showEditUserPage&uId=%d&", userId) + queryString, false);
    }
}
