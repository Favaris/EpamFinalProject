package com.prusan.finalproject.web.command.category;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.util.DownloadAllCategoriesCommand;
import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(UpdateCategoryCommand.class);

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
            return new Chain(Pages.ERROR_JSP, false);
        }

        req.setAttribute("nextChain", new Chain(Pages.CATEGORIES_JSP, false));
        return new DownloadAllCategoriesCommand().execute(req, resp);
    }
}
