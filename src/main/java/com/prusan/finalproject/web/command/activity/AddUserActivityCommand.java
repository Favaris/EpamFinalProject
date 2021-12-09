package com.prusan.finalproject.web.command.activity;

import com.prusan.finalproject.db.entity.UserActivity;
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

/**
 * Adds a new user activity by user id and activity id. This activity is already set accepted.
 * <pre>
 *     Retrieves next request params:
 *     'uId' - user id;
 *     'aId' - activity id;
 * </pre>
 */
public class AddUserActivityCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();

        int userId = Integer.parseInt(req.getParameter("uId"));
        log.debug("received a userId={}", userId);
        int activityId = Integer.parseInt(req.getParameter("aId"));
        log.debug("received an activityId={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        UserActivityService uas = sf.getUserActivityService();

        try {
            UserActivity ua = UserActivity.createAcceptedWithIds(userId, activityId);
            log.debug("created a stub for user activity: {}", ua);

            uas.save(ua);
            log.debug("successfully added a new user activity with userId={}, activityId={}", userId, activityId);

            String queryString = handler.getQueryString(session, true, true, true, false);

            return Chain.createRedirect(String.format("controller?command=%s&uId=%d&", CommandContainer.CommandNames.SHOW_ADD_ACTIVITIES_FOR_USER_PAGE, userId) + queryString);
        } catch (ServiceException e) {
            log.error("failed to get a user activity by activityId={}", activityId, e);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
