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
        Chain nextChain = new Chain("controller?command=downloadAllActivities", true);
        req.setAttribute("nextChain", nextChain);
        log.debug("set attribute 'nextChain' as {}", nextChain);
        return new DownloadAllCategoriesCommand().execute(req, resp);
    }
}
