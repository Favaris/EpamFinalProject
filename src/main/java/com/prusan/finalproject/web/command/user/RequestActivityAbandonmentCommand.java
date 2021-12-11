package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
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

public class RequestActivityAbandonmentCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        Integer userId = Integer.valueOf(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        Integer activityId = Integer.valueOf(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);

        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();

        try {
            UserActivity ua = uas.get(userId, activityId);
            log.debug("successfully retrieved a user activity instance: {}", ua);
            ua.setRequestedAbandon(true);
            uas.update(ua);
            log.debug("successfully updated a user activity {}", ua);
        } catch (NoSuchActivityException ex) {
            log.debug("unable to get a user activity instance with userId={}, activityId={}", userId, activityId, ex);
            req.setAttribute("err_msg", ex.getMessage());
        } catch (ServiceException e) {
            log.debug("unable to get a user activity instance with userId={}, activityId={} or to update it", userId, activityId, e);
            req.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);

        return Chain.createRedirect(referer);
    }
}
