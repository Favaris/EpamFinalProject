package com.prusan.finalproject.web.command.util;

import com.prusan.finalproject.web.Chain;
import com.prusan.finalproject.web.command.Command;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrepareForShowingAllActivitiesCommand implements Command {
    private static final Logger log = LogManager.getLogger(PrepareForShowingAllActivitiesCommand.class);

    @Override
    public Chain execute(HttpServletRequest req, HttpServletResponse resp) {
//        String page = req.getParameter("page");
//        log.debug("retrieved a page number: {}", page);
//        String pageSize = req.getParameter("pageSize");
//        log.debug("retrieved a page size: {}", pageSize);
//        String orderBy = req.getParameter("orderBy");
//        log.debug("retrieved an orderBy param: {}", orderBy);
//
//        if (orderBy == null) {
//            orderBy = "name";
//            log.debug("set default ordering by {}", orderBy);
//        }
//
//        Chain nextChain = new Chain(String.format("controller?command=downloadAllActivities&page=%s&pageSize=%s&orderBy=%s", page, pageSize, orderBy), true);
        Chain nextChain = new Chain("controller?command=downloadAllActivities", true);
        req.setAttribute("nextChain", nextChain);
        log.debug("set attribute 'nextChain' as {}", nextChain);
        return new DownloadAllCategoriesCommand().execute(req, resp);
    }
}
