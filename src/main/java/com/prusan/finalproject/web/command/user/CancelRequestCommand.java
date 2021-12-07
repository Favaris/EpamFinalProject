package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
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

/**
 * Cancels a user's request for acceptance or abandonment. Retrieves a user id as parameter 'uId' and an activity id as a parameter 'aId'.
 */
public class CancelRequestCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        UserActivityService uas = sf.getUserActivityService();

        try {
            UserActivity ua = uas.get(userId, activityId);
            log.debug("retrieved a user activity {}", ua);
            if (ua.isRequestedAbandon()) {
                ua.setRequestedAbandon(false);
                uas.update(ua);
                log.debug("removed user activity {} from requested for abandonment", ua);
            } else {
                uas.delete(userId, activityId);
                log.debug("removed user activity {} from requested for acceptance", ua);
            }
            String query = handler.getQueryString(req.getSession());
            log.debug("received a query string: '{}'", query);
            return Chain.createRedirect("controller?command=showUsersRequests&" + query);
        } catch (NoSuchActivityException ex) {
            log.debug("unable to find a user activity by userId={} and activityId={}", userId, activityId);
            req.getSession().setAttribute("err_msg", ex.getMessage());
            return Chain.getErrorPageChain();
        } catch (ServiceException e) {
            log.debug("an error occurred when trying to download and change user activity by userId={} and activityId={}", userId, activityId);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
