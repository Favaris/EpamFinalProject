package com.prusan.finalproject.web.command.category;

import com.prusan.finalproject.db.entity.Category;
import com.prusan.finalproject.db.service.CategoryService;
import com.prusan.finalproject.db.service.exception.NameIsTakenException;
import com.prusan.finalproject.db.service.exception.ServiceException;
import com.prusan.finalproject.db.util.ServiceFactory;
import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.PaginationAttributesHandler;
import com.prusan.finalproject.web.Validator;
import com.prusan.finalproject.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AddCategoryCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();
    private static final Validator validator = Validator.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        log.debug("retrieved a category name:{}", name);

        String referer = req.getHeader("referer");
        log.debug("retrieved a referer string: '{}'", referer);
        Category cat = new Category(name);

        HttpSession session = req.getSession();
        if (!validator.validate(Validator.CATEGORY_NAME, name)) {
            session.setAttribute("invalidInputError", "name");
            session.setAttribute("invalidAddCategory", cat);
            log.debug("name field is invalid, set up corresponding error message");
            return Chain.createRedirect(referer);
        }

        CategoryService cs = ServiceFactory.getInstance().getCategoryService();
        log.debug("created a new category instance: {}", cat);

        try {
            cs.add(cat);
            log.debug("successfully saved a new category {}", cat);
        } catch (NameIsTakenException ex) {
            log.debug("can not save category {}, this name is already taken", cat, ex);
            session.setAttribute("invalidAddCategory", cat);
            session.setAttribute("addCategoryErrMsg", ex.getMessage());
        } catch (ServiceException e) {
            log.error("unable to save the category {}", cat, e);
            session.setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }

        return Chain.createRedirect(referer);
    }


}
