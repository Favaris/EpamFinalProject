package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
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
import java.util.Set;
import java.util.stream.Collectors;

public class DownloadActivitiesForUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req);
        String[] filterBy = handler.getFilterByFromParameters(req);

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            List<Activity> activities;
            // by setting start == 0 and end == Integer.MAX_VALUE, we get a list of all activities for this user
            activities = as.getActivitiesNotTakenByUser(userId, 0, Integer.MAX_VALUE, orderBy, filterBy);
            log.debug("downloaded all activities not taken by a user with id {}, list size={}", userId, activities.size());

            List<UserActivity> userActivities = (List<UserActivity>) req.getSession().getAttribute("userToEditActivities");
            if (userActivities != null) {
                log.debug("retrieved a list of user's activities: {}", userActivities);
                activities = activities.stream()
                        .filter(a -> !userActivities.contains(a))
                        .collect(Collectors.toList());
                log.debug("filtered out already taken user activities from activities list, list size: {}", activities.size());
            }

            List<Activity> paginatedActivities = handler.getPaginationSublist(activities, page, pageSize);

            req.setAttribute("activities", paginatedActivities);
            log.debug("set request attribute 'activities'");
            handler.setPaginationParametersAsRequestAttributes(req, activities.size(), pageSize, page, orderBy, filterBy);
            return new Chain(Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP, true);
        } catch (ServiceException e) {
            log.error("failed to upload a list of activities for uId={}, page={}, pageSize={}, orderBy={}, filterBy={}", userId, page, pageSize, orderBy, Arrays.toString(filterBy), e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
