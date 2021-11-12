package com.prusan.finalproject.web.command;

import com.prusan.finalproject.web.Chain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    Chain execute(HttpServletRequest req, HttpServletResponse resp);
}
