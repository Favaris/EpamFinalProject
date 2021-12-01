package com.prusan.finalproject.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This class is used by Command objects. It contains meta information such as url on which to go next and on whether to do it by forward or by sendRedirect.
 */
public class Chain {
    private static final Logger log = LogManager.getLogger(Chain.class);

    private final String url;
    private final boolean doForward;

    @Override
    public String toString() {
        return "Chain{" +
                "url='" + url + '\'' +
                ", doForward=" + doForward +
                '}';
    }

    public Chain(String url, boolean doForward) {
        this.url = url;
        this.doForward = doForward;
        log.debug("created new chain {}", this);
    }

    public String getUrl() {
        return url;
    }

    public boolean isDoForward() {
        return doForward;
    }

    /**
     * Moves forth by the given chain url, using send redirect or forward methods.
     */
    public void moveForth(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (doForward) {
            log.debug("moved forward to '{}'", url);
            req.getRequestDispatcher(url).forward(req, resp);
        } else {
            log.debug("sent redirect to '{}'", url);
            resp.sendRedirect(url);
        }
    }
}
