package com.prusan.finalproject.web.command.category;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
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

public class UpdateCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved a category name: '{}'", name);
        int id = Integer.parseInt(req.getParameter("id"));
        log.debug("retrieved a category id: {}", id);

        Category cat = new Category(id, name);

        CategoryService cs = ServiceFactory.getInstance().getCategoryService();
        try {
            cs.update(cat);
            log.debug("successfully updated a category {}", cat);
        } catch (NameIsTakenException ex) {
            log.debug("unable to update a category {}", cat);
            req.getSession().setAttribute("err_msg", ex.getMessage());
        } catch (ServiceException e) {
            log.error("unable to update a category {}", cat);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        String queryString = handler.getPaginationQueryString(req.getSession());
        log.debug("received a url params string: '{}'", queryString);

        return Chain.createRedirect(String.format("controller?command=%s&" + queryString, CommandContainer.CommandNames.SHOW_CATEGORIES_PAGE));
    }
}
