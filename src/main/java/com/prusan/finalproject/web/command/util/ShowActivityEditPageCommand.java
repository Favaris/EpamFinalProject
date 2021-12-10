package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Activity;
import com.prusan.finalproject.db.service.ActivityService;
import com.prusan.finalproject.db.service.exception.NoSuchActivityException;
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

/**
 * Downloads an activity by its id, and all categories. Places them as a session attributes 'activityToEdit' and 'categories'.
 */
public class ShowActivityEditPageCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final CommandUtils commandUtils = CommandUtils.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {

        int activityId = Integer.parseInt(req.getParameter("id"));
        log.debug("retrieved an activityId={}", activityId);

        ServiceFactory sf = ServiceFactory.getInstance();
        ActivityService as = sf.getActivityService();

        try {
            commandUtils.setAllCategoriesInRequestAttribute(req);

            if (req.getSession().getAttribute("invalidActivity") != null) {
                log.debug("'invalidActivity' attribute is present, not downloading an activity");
                return Chain.createForward(Pages.ACTIVITY_EDIT_PAGE_JSP);
            }

            Activity activity = as.getById(activityId);
            log.debug("retrieved an activity {}", activity);
            req.setAttribute("activityToEdit", activity);
            log.debug("set a request attribute 'activityToEdit'");
            return Chain.createForward(Pages.ACTIVITY_EDIT_PAGE_JSP);
        } catch (NoSuchActivityException e) {
            log.debug("no such activity with id={}", activityId, e);
            req.getSession().setAttribute("err_msg", "Activity you are looking for does not exist");
            return Chain.getErrorPageChain();
        } catch (ServiceException e) {
            log.error("can not get an activity by id={}", activityId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
