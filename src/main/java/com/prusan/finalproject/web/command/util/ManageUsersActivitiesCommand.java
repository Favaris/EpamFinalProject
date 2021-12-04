package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
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
import java.util.List;

public class ManageUsersActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req);
        String[] filterBy = handler.getFilterByFromParameters(req);

        CommandUtils cu = CommandUtils.getInstance();
        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();

        try {
            cu.setAllCategoriesInRequestAttribute(req);

            List<UserActivity> userActivities = uas.getAcceptedForUser(userId, pageSize * (page - 1), pageSize * page, orderBy, filterBy);
            log.debug("received a list of all accepted activities for user with id={}", userId);

            int entitiesCount = uas.getActivitiesCountForUser(userId);
            log.debug("received amount of activities for user with id = {}, amount = {}", userId, entitiesCount);

            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, orderBy, filterBy);

            req.setAttribute("usersActivities", userActivities);
            log.debug("set a list of activities as a request attribute 'usersActivities', list size={}", userActivities);

            req.setAttribute("uId", userId);
            log.debug("set a 'uId'={} as request attribute", userId);

            return new Chain(Pages.MANAGE_USERS_ACTIVITIES_PAGE_JSP, true);
        } catch (ServiceException e) {
            log.error("failed to download all user's activities, userId={}", userId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
