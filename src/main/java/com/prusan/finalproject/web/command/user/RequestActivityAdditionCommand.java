package com.prusan.finalproject.web.command.user;

import com.prusan.finalproject.db.entity.UserActivity;
import com.prusan.finalproject.db.service.UserActivityService;
import com.prusan.finalproject.db.service.exception.DependencyAlreadyExistsException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Command for adding an activity for user.
 */
public class RequestActivityAdditionCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();
    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession s = req.getSession();

        Integer userId = Integer.valueOf(req.getParameter("uId"));
        log.debug("retrieved a user id={}", userId);
        Integer activityId = Integer.valueOf(req.getParameter("aId"));
        log.debug("retrieved an activity id={}", activityId);
        UserActivity ua = UserActivity.createStubWithIds(userId, activityId);

        UserActivityService uas = ServiceFactory.getInstance().getUserActivityService();
        try {
            uas.add(ua);
        } catch (DependencyAlreadyExistsException e) {
            log.debug("this user activity already exists");
            req.setAttribute("err_msg", "this user activity already exists");
            return Chain.getErrorPageChain();
        } catch (ServiceException e) {
            log.error("error: ", e);
            req.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);

        return Chain.createRedirect(referer);
    }
}
