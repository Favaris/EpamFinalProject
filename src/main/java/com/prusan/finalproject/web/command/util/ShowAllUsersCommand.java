package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.db.entity.User;
import com.prusan.finalproject.db.service.UserService;
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
 * Downloads all users with role 'user' as List in request attribute 'usersList'.
 */
public class ShowAllUsersCommand implements Command {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final PaginationAttributesHandler handler = PaginationAttributesHandler.getInstance();

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
        UserService us = ServiceFactory.getInstance().getUserService();

        int page = handler.getPageFromParameters(req);
        int pageSize = handler.getPageSizeFromParameters(req);
        String orderBy = handler.getOrderByFromParameters(req, "userLogin");
        String countLessThen = handler.getCountLessThenFromParameters(req);
        String countBiggerThen = handler.getCountBiggerThenFromParameters(req);
        String searchBy = handler.getSearchByFromParameters(req);

        try {
            List<User> usersList = us.getWithRoleUser(pageSize * (page - 1), pageSize, orderBy, countLessThen, countBiggerThen, searchBy);
            log.debug("got a usersList, list size: {}", usersList.size());
            int usersCount = us.getDefaultUsersCount();
            log.debug("received a users amount: {}", usersCount);

            req.setAttribute("usersList", usersList);
            handler.setPaginationParametersAsRequestAttributes(req, usersCount, pageSize, page, orderBy, null);
            req.setAttribute("countLessThen", countLessThen);
            req.setAttribute("countBiggerThen", countBiggerThen);
            req.setAttribute("searchBy", searchBy);
            log.debug("set up all needed request params");
            return Chain.createForward(Pages.USERS_JSP);
        } catch (ServiceException e) {
            log.error("error while trying to download all users with role='user'", e);
            req.getSession().setAttribute("err_msg", e.getMessage());
            return Chain.getErrorPageChain();
        }
    }
}
