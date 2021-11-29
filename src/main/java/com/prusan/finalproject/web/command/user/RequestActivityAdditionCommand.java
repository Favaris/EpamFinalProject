package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
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

/**
 * Command for adding an activity for user.
 */
public class RequestActivityAdditionCommand implements Command {
    private static final Logger log = LogManager.getLogger(RequestActivityAdditionCommand.class);
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession();

        Integer userId = Integer.valueOf(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        Integer activityId = Integer.valueOf(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);
        UserActivity ua = new UserActivity();
        ua.setUserId(userId);
        ua.setActivityId(activityId);

        ActivityService as = ServiceFactory.getInstance().getActivityService();
        try {
            as.addUserActivity(ua);
        } catch (DependencyAlreadyExistsException e) {
            log.debug("this user activity already exists");
            req.setAttribute("err_msg", "this user activity already exists");
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("error: ", e);
            req.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }

        String urlParams = handler.getURLParametersStringWithSortingParams(req);
        log.debug("received a url params string: '{}'", urlParams);

        return new Chain("controller?command=showActivitiesPage&" + urlParams, false);
    }
}
