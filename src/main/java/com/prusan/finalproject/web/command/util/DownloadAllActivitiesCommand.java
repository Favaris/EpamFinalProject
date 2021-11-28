package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
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
 * Intermediate command for setting a list of all categories as the request attribute 'activities'.
 * <pre>
 *   If user role is 'admin', loads all activities if there is no request parameter 'uId'.
 *   If there is a param 'uId' and user is admin, that means that admin wants to add a new activity to a certain user, so the command downloads all activities that are not taken by user with such id;
 *   If the role is 'user', loads only activities that are not already taken by this user.
 * </pre>

 */
public class DownloadAllActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = Integer.parseInt(req.getParameter("page"));
        log.debug("retrieved a page number: {}", page);
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        log.debug("retrieved a page size: {}", pageSize);
        String orderBy = req.getParameter("orderBy");
        log.debug("retrieved an orderBy param: {}", orderBy);

        if (orderBy == null) {
            orderBy = "name";
            log.debug("set default ordering by {}", orderBy);
        }

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        User u = (User) req.getSession().getAttribute("user");
        if (u == null) {
            return new Chain(Pages.SIGN_IN_JSP, false);
        }

        try {
            List<Activity> activities;
            int entitiesCount = 0;

            if ("admin".equals(u.getRole())) {
                log.debug("user {} is admin", u);

                // case when an admin wants to add some activities for a user
                if (req.getParameter("uId") != null) {
                    return processAddingNewActivitiesForUserByAdminCase(req, as);
                }
                log.debug("did not find a param 'uId'");

                activities = as.getActivities(pageSize * (page - 1), pageSize, orderBy);
                log.debug("received a list of activities ordered by '{}' for page={} with pageSize={}", orderBy, page, pageSize);
                entitiesCount = as.getActivitiesCount();
            } else {
                log.debug("user {} is default user", u);
                activities = as.getAllActivitiesNotTakenByUser(u.getId());
                log.debug("downloaded a list of all activities not taken by a user {}, list size={}", u, activities.size());
            }

            int pageCount = entitiesCount / pageSize == 0 ? 1 : entitiesCount / pageSize;
            req.setAttribute("activities", activities);
            log.debug("set activities list as a request attribute");
            req.setAttribute("pageCount" , pageCount);
            log.debug("set a pageCount attribute: '{}'", pageCount);
            req.setAttribute("page", page);
            log.debug("set a page attribute: '{}'", page);
            req.setAttribute("orderBy", orderBy);
            log.debug("set an orderBy attribute: '{}'", orderBy);

            return new Chain(Pages.ACTIVITIES_JSP, true);
        } catch (ServiceException e) {
            log.error("can not get all activities", e);
            req.getSession().setAttribute("err_msg", "can not get all activities");
            return new Chain(Pages.ERROR_JSP, true);
        }

    }

    private Chain processAddingNewActivitiesForUserByAdminCase(HttpServletRequest req, ActivityService as) throws ServiceException {
        List<Activity> activities;
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a parameter uId={}", userId);
        activities = as.getAllActivitiesNotTakenByUser(userId);
        log.debug("downloaded all activities not taken by a user with id {}, list size={}", userId, activities.size());
        req.setAttribute("activities", activities);
        log.debug("sending admin to {}", Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP);
        return new Chain(Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP, true);
    }
}
