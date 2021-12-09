package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class DeleteUserActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();

        try {
            uas.delete(userId, activityId);
            log.debug("successfully deleted a user activity by userId={}, activityId={}", userId, activityId);

            String queryString = handler.getQueryString(session, true, true, true, false);
            log.debug("received a query string: '{}'", queryString);

            return Chain.createRedirect(String.format("controller?command=%s&uId=%d&", CommandContainer.CommandNames.MANAGE_USERS_ACTIVITIES, userId) + queryString);
        } catch (ServiceException e) {
            log.error("failed to delete a user activity by userId={}, activityId={}", userId, activityId);
            return Chain.getErrorPageChain();
        }
    }
}
