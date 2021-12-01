package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.UserService;
import com.prusan.finalproject.db.service.exception.NoSuchUserException;
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
import java.util.List;

/**
 * Downloads all info about the user by its id. Retrieves this id from request attribute 'id'. Downloads user and his running activities in session attributes as 'userToEdit' and 'userToEditActivities'.
 */
public class PrepareForUserEditingCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForUserEditingCommand.class);
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved user id from request: {}", userId);
        HttpSession session = req.getSession();

        User userToEdit = (User) session.getAttribute("userToEdit");
        List<UserActivity> userActivities = (List<UserActivity>) session.getAttribute("userToEditActivities");
        if (userToEdit != null && userActivities != null && userToEdit.getId() == userId) {
            log.debug("retrieved a 'userToEdit' attribute '{}'", userToEdit);
            log.debug("retrieved a 'userActivities' attribute, list size: {}", userActivities.size());
            log.debug("'userToEdit' attribute's id matches with 'userId' parameter and userActivities are not null, consider all needed data is up-to-date");
            processPaginationParameters(req, userActivities);
            return new Chain(Pages.USER_EDIT_PAGE_JSP, true);
        }

        ServiceFactory sf = ServiceFactory.getInstance();
        UserService us = sf.getUserService();
        ActivityService as = sf.getActivityService();

        try {
            User u = us.getById(userId);
            log.debug("retrieved a user {}", u);
            userActivities = as.getAllRunningUsersActivities(userId);
            log.debug("retrieved user's activities list, list size: {}", userActivities.size());
            session.setAttribute("userToEdit", u);
            session.setAttribute("userToEditActivities", userActivities);
            log.debug("set retrieved entities as request attributes");

            processPaginationParameters(req, userActivities);
            return new Chain(Pages.USER_EDIT_PAGE_JSP, true);
        } catch (NoSuchUserException ex) {
            log.debug("no such user with id={}", userId);
            session.setAttribute("err_msg", "User not exists");
            return new Chain(Pages.ERROR_JSP, false);
        } catch (ServiceException e) {
            log.error("error while trying to load a user by id={}", userId, e);
            session.setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }
    }

    private void processPaginationParameters(HttpServletRequest req, List<UserActivity> userActivities) {
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        handler.setPaginationParametersAsRequestAttributes(req, userActivities.size(), pageSize, page, null, null);
        List<UserActivity> paginatedList = handler.getPaginationSublist(userActivities, page, pageSize);
        req.setAttribute("paginatedActivities", paginatedList);
        log.debug("retrieved a paginated list with size={}, set it as the req attribute 'paginatedActivities'", paginatedList.size());
    }
}
