package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
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

/**
 * Intermediate command. Downloads all activities that belong to a certain user by his id.
 */
public class ShowRunningActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();
    private static final CommandUtils commandUtils = CommandUtils.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req);
        String[] filterBy = handler.getFilterByFromParameters(req);

        User u = (User) req.getSession().getAttribute("user");
        log.debug("retrieved a user from the session: {}", u);

        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();
        try {
            commandUtils.setAllCategoriesInRequestAttribute(req);

            List<UserActivity> list = uas.getAcceptedForUser(u.getId(), pageSize * (page - 1), pageSize, orderBy, filterBy);
            log.debug("got a list of running activities, list size: {}", list.size());

            int entitiesCount = uas.getActivitiesCountForUser(u.getId());
            log.debug("received amount of activities for user {}, amount={}", u, entitiesCount);

            req.setAttribute("runningActivities", list);
            log.debug("set a request attribute 'runningActivities'");
            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, orderBy, filterBy);

            return Chain.createForward(Pages.RUNNING_ACTIVITIES_JSP);
        } catch (ServiceException e) {
            log.error("error in #execute(), user={}", u, e);
            req.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
