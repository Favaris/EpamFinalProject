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

public class AddCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(AddCategoryCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved a category name:{}", name);
        Category cat = new Category(name);
        log.debug("created a new category instance: {}", cat);

        CategoryService cs = ServiceFactory.getInstance().getCategoryService();

        try {
            cs.save(cat);
            log.debug("successfully saved a new category {}", cat);
        } catch (NameIsTakenException ex) {
            log.debug("can not save category {}, this name is already taken", cat, ex);
            req.getSession().setAttribute("err_msg", ex.getMessage());
        } catch (ServiceException e) {
            log.error("unable to save the category {}", cat, e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return new Chain(Pages.ERROR_JSP, false);
        }

        return new Chain("controller?command=showCategoriesPage", false);
    }
}
