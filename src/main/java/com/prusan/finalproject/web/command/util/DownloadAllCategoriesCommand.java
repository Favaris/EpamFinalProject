package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
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
public class DownloadAllCategoriesCommand implements Command {
    private static final Logger log = LogManager.getLogger(DownloadAllCategoriesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        CategoryService cs = ServiceFactory.getInstance().getCategoryService();
        try {
            List<Category> cats = cs.getAll();
            log.debug("retrieved a list of all categories, list size: {}", cats.size());
            log.debug("placed a list of all categories as a request atr");

            if (req.getAttribute("nextChain") != null) {
                Chain nextChain = (Chain) req.getAttribute("nextChain");
                req.removeAttribute("nextChain");
                if (nextChain.isDoForward()) {
                    req.setAttribute("categories", cats);
                } else {
                    req.getSession().setAttribute("categories", cats);
                }
                log.debug("retrieved a next Chain {}", nextChain);
                return nextChain;
            }

            req.getSession().setAttribute("categories", cats);
            return new Chain(Pages.CATEGORIES_JSP, true);
        } catch (ServiceException e) {
            log.error("unable to get all activities", e);
            req.getSession().setAttribute("err_msg", "can not download all categories");
            return new Chain(Pages.ERROR_JSP, false);
        }
    }
}
