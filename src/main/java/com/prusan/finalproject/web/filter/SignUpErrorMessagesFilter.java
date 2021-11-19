package com.prusan.finalproject.web.filter;

import com.prusan.finalproject.web.command.SignUpCommand;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter that clears all error messages that were set while signing in.
 * <pre>NOT USED.</pre>
 */
@WebFilter("/SignUpErrorMessagesFilter")
public class SignUpErrorMessagesFilter implements Filter {
    private static final Set<String> messageNames;

    static {
        messageNames = new HashSet<>();
        messageNames.add(SignUpCommand.LOGIN_ERROR_MESSAGE);
        messageNames.add(SignUpCommand.PASSWORD_ERROR_MESSAGE);
        messageNames.add(SignUpCommand.NAME_ERROR_MESSAGE);
        messageNames.add(SignUpCommand.SURNAME_ERROR_MESSAGE);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        for (String attrName : messageNames) {
            request.removeAttribute(attrName);
        }
        chain.doFilter(request, response);
    }
}
