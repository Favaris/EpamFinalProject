package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.ActivityService;
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
import java.util.Arrays;
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
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req);
        String[] filterBy = handler.getFilterByFromParameters(req);

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
                activities = getActivitiesForAdmin(page, pageSize, orderBy, filterBy, as);
            } else {
                log.debug("user {} is default user", u);
                activities = getActivitiesForUser(page, pageSize, orderBy, filterBy, as, u);
            }

            entitiesCount = as.getActivitiesCount(u.getId(), filterBy);
            log.debug("received a number of all entities filtered by {}", Arrays.toString(filterBy));

            req.setAttribute("activities", activities);
            log.debug("set activities list as a request attribute");

            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, orderBy, filterBy);

            return new Chain(Pages.ACTIVITIES_JSP, true);
        } catch (ServiceException e) {
            log.error("can not get all activities", e);
            req.getSession().setAttribute("err_msg", "can not get all activities");
            return new Chain(Pages.ERROR_JSP, false);
        }

    }

    private List<Activity> getActivitiesForUser(int page, int pageSize, String orderBy, String[] filterBy, ActivityService as, User u) throws ServiceException {
        List<Activity> activities;
        activities = as.getActivitiesNotTakenByUser(u.getId(), pageSize * (page - 1), pageSize, orderBy, filterBy);
        log.debug("downloaded a list of all activities not taken by a user {}, list size={}", u, activities.size());
        if (activities.size() == 0 && page > 1) {
            log.debug("activities list is empty while page={}, reducing the page value and reloading the sublist", page);
            --page;
            activities = getActivitiesForUser(page, pageSize, orderBy, filterBy, as, u);
        }
        return activities;
    }

    private List<Activity> getActivitiesForAdmin(int page, int pageSize, String orderBy, String[] filterBy, ActivityService as) throws ServiceException {
        List<Activity> activities;
        activities = as.getActivities(pageSize * (page - 1), pageSize, orderBy, filterBy);
        log.debug("received a list of activities ordered by '{}' for page={} with pageSize={}", orderBy, page, pageSize);
        if (activities.size() == 0 && page > 1) {
            log.debug("activities list is empty while page={}, reducing the page value and reloading the sublist", page);
            --page;
            activities = getActivitiesForAdmin(page, pageSize, orderBy, filterBy, as);
        }
        return activities;
    }


}
