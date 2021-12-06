package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.CommandUtils;
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

public class ShowAddActivitiesForUserPageCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();
    private static final CommandUtils commandUtils = CommandUtils.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id from parameters: {}", userId);
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req);
        String[] filterBy = handler.getFilterByFromParameters(req);

        ActivityService as = ServiceFactory.getInstance().getActivityService();

        try {
            commandUtils.setAllCategoriesInRequestAttribute(req);

            List<Activity> activities = as.getActivitiesNotTakenByUser(userId, pageSize * (page - 1), pageSize, orderBy, filterBy);
            log.debug("received a list of activities not taken by a user #{}, list size: {}", userId, activities.size());
            if (activities.isEmpty() && page > 1) {
                log.debug("activities list is empty, reducing page by 1");
                log.debug("page={}", --page);
                activities = as.getActivitiesNotTakenByUser(userId, pageSize * (page - 1), pageSize, orderBy, filterBy);
                log.debug("received a list of activities not taken by a user #{}, list size: {}", userId, activities.size());
            }

            int entitiesCount = as.getActivitiesCount(userId, filterBy);
            log.debug("received an amount of all activities not taken by a user with id={}, amount ={}", userId, entitiesCount);

            req.setAttribute("notTakenActivities", activities);
            req.setAttribute("uId", userId);
            log.debug("set up all request attributes");

            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, orderBy, filterBy);

            return Chain.createForward(Pages.ADD_ACTIVITIES_FOR_USER_PAGE_JSP);
        } catch (ServiceException e) {
            log.error("failed to upload a list of activities for uId={}, page={}, pageSize={}, orderBy={}, filterBy={}", userId, page, pageSize, orderBy, Arrays.toString(filterBy), e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
