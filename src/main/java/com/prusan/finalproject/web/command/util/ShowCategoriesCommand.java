package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
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
import java.util.List;

/**
 * Downloads a list of all categories and places them as the request attribute 'categories'.
 */
public class ShowCategoriesCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);

        CategoryService cs = ServiceFactory.getInstance().getCategoryService();
        try {
            List<Category> cats = cs.getCategories(pageSize * (page - 1), pageSize);
            log.debug("received a list of categories, list size: {}", cats.size());

            if (cats.size() == 0 && page > 1) {
                --page;
                cats = cs.getCategories(pageSize * (page - 1), pageSize);
                log.debug("current categories list was empty while the page was bigger then 1, reloaded it with decreased page, page={}, list size = {}", page, cats.size());
            }

            int entitiesCount = cs.getCount();
            log.debug("received an amount of categories: {}", entitiesCount);
            handler.setPaginationParametersAsRequestAttributes(req, entitiesCount, pageSize, page, null, null);

            req.getSession().setAttribute("categories", cats);
            log.debug("set a request attribute 'categories', list size={}", cats.size());
            return Chain.createForward(Pages.CATEGORIES_JSP);
        } catch (ServiceException e) {
            log.error("unable to get all activities", e);
            req.getSession().setAttribute("err_msg", "can not download all categories");
            return Chain.getErrorPageChain();
        }
    }
}
