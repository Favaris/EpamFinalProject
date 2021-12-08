package com.prusan.finalproject.web.tag;

import com.prusan.finalproject.web.constant.Pages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.util.*;

/**
 * Tag that checks the role with permissions for this role. If role is not allowed to be present on certain page (where this tag was called), then it will be sent redirect to corresponding page (see destinations field).
 */
public class SecurityTag extends SimpleTagSupport {
    private static final Logger log = LogManager.getLogger(Thread.currentThread().getStackTrace()[1].getClassName());
    private static final Map<String, List<String>> permissions;
    private static final Map<String, String> destinations;

    static {
        permissions = new HashMap<>();
        List<String> all = Arrays.asList("", "user", "admin");
        List<String> guest = Collections.singletonList("");
        List<String> user = Collections.singletonList("user");
        List<String> logged = Arrays.asList("user", "admin");
        List<String> admin = Collections.singletonList("admin");

        permissions.put("all", all);
        permissions.put("guest", guest);
        permissions.put("logged", logged);
        permissions.put("admin", admin);
        permissions.put("user", user);

        destinations = new HashMap<>();
        destinations.put("", Pages.SIGN_IN_JSP);
        destinations.put("user", Pages.HOME_JSP);
        destinations.put("admin", Pages.HOME_JSP);
    }

    private String role;

    private String permission;

    public void setRole(String role) {
        this.role = role;
        log.debug("got set role '{}'", role);
    }

    public void setPermission(String permission) {
        this.permission = permission;
        log.debug("got set permission '{}'", permission);
    }

    @Override
    public void doTag() throws JspException, IOException {
        PageContext pageContext = (PageContext) getJspContext();
        log.debug("retrieved a page context instance {}", pageContext);
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        log.debug("retrieved a servlet request instance {}", req);
        HttpServletResponse resp = (HttpServletResponse) pageContext.getResponse();
        log.debug("retrieved a servlet response instance {}", resp);

        List<String> roles = permissions.get(permission);
        if (roles == null) {
            log.error("no such permission '{}'", permission);
            req.getSession().setAttribute("err_msg", "No such permission '" + permission + "'");
            resp.sendRedirect(req.getServletContext().getContextPath() + "/" + Pages.ERROR_JSP);
            return;
        }

        log.debug("retrieved a list of roles with permission {}, list size: {}", permission, roles.size());
        if (roles.contains(role)) {
            log.debug("access granted for user {}", req.getSession().getAttribute("user"));
        } else {
            log.debug("access denied for user {}", req.getSession().getAttribute("user"));
            String address = req.getServletContext().getContextPath() + "/" + destinations.get(role);
            log.debug("sent user to {}", address);
            resp.sendRedirect(address);
        }
    }
}
