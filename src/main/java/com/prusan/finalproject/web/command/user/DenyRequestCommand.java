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

/**
 * A command for denying user requests for accepting or abandoning an activity.
 */
public class DenyRequestCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("retrieved a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("retrieved an activityId={}", activityId);

        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();
        try {
            UserActivity ua = uas.get(userId, activityId);

            log.debug("retrieved a user activity {}", ua);

            if (!ua.isAccepted()) {
                uas.delete(userId, activityId);
                log.debug("denied adding a user activity {}, such row is deleted", ua);
            } else {
                ua.setRequestedAbandon(false);
                uas.update(ua);
                log.debug("denied abandonment for a user activity {}", ua);
            }
            String referer = req.getHeader("referer");
            log.debug("retrieved a referer string: '{}'", referer);

            return Chain.createRedirect(referer);
        } catch (NoSuchActivityException e) {
            log.debug("no such user activity with userId={}, activityId={}", userId, activityId);
            req.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        } catch (ServiceException e) {
            log.error("unable to download corresponding user activity: userId={}, activityId={}", userId, activityId);
            req.getSession().setAttribute("err_msg", "can not download corresponding user activity");
            return Chain.getErrorPageChain();
        }
    }

}
