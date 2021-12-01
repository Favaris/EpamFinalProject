package com.prusan.finalproject.web.command.category;

import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.FailedCategoryDeletionException;
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

public class DeleteCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(DeleteCategoryCommand.class);
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

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
            return new Chain(Pages.ERROR_JSP, false);
        }

        String queryString = handler.getQueryString(req.getSession());
        log.debug("received a url params string: '{}'", queryString);

        return new Chain("controller?command=showCategoriesPage&" + queryString, false);
    }
}
