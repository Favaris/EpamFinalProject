package com.prusan.finalproject.web.command.category;

import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.FailedCategoryDeletionException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int catId = Integer.parseInt(req.getParameter("id"));
        log.debug("retrieved a category id={}", catId);

        CategoryService cs = ServiceFactory.getInstance().getCategoryService();

        try {
            cs.delete(catId);
            log.debug("successfully deleted a category by id={}", catId);
        } catch (FailedCategoryDeletionException e) {
            log.debug("unable to delete a category by id={}", catId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
        }
        catch (ServiceException e) {
            log.error("failed to delete a category by id={}", catId, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);

        return Chain.createRedirect(referer);
    }
}
