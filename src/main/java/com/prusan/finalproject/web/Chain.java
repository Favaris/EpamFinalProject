package com.prusan.finalproject.web;

import com.prusan.finalproject.web.constant.Pages;
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
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Chain errorPageChain = new Chain(Pages.ERROR_JSP, false);

    private final String url;
    private final boolean doForward;

    public static Chain getErrorPageChain() {
        log.debug("returned an error page instance");
        return errorPageChain;
    }

    public static Chain createRedirect(String url) {
        Chain chain = new Chain(url, false);
        log.debug("returned a redirect instance: {}", chain);
        return chain;
    }

    public static Chain createForward(String url) {
        Chain chain = new Chain(url, true);
        log.debug("returned a forward instance: {}", chain);
        return chain;
    }


    @Override
    public String toString() {
        return "Chain{" +
                "url='" + url + '\'' +
                ", doForward=" + doForward +
                '}';
    }

    private Chain(String url, boolean doForward) {
        this.url = url;
        this.doForward = doForward;
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
