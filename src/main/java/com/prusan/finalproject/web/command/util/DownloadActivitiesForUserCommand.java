package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
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
import java.util.Arrays;
import java.util.List;

public class DownloadActivitiesForUserCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadActivitiesForUserCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = Integer.parseInt(req.getParameter("page"));
        log.debug("received a page number: {}", page);
        int pageSize = Integer.parseInt(req.getParameter("pageSize"));
        log.debug("received a page size: {}", pageSize);
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a parameter uId={}", userId);
        String orderBy = req.getParameter("orderBy");
        log.debug("received an orderBy param: {}", orderBy);
        String[] filterBy = req.getParameterValues("filterBy");

        if (orderBy == null) {
            orderBy = "name";
            log.debug("set ordering by '{}'", orderBy);
        }

        if (filterBy == null) {
            filterBy = new String[] {"all"};
            log.debug("set filtering 'all'");
        }

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            List<Activity> activities;
            activities = as.getActivitiesNotTakenByUser(userId, pageSize * (page - 1), pageSize, orderBy, filterBy);
            log.debug("downloaded all activities not taken by a user with id {}, list size={}", userId, activities.size());
            req.setAttribute("activities", activities);
            log.debug("sending admin to {}", Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP);
            return new Chain(Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP, true);
        } catch (ServiceException e) {
            log.error("failed to upload a list of activities for uId={}, page={}, pageSize={}, orderBy={}, filterBy={}", userId, page, pageSize, orderBy, Arrays.toString(filterBy), e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
