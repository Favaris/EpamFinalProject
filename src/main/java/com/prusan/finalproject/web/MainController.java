package com.prusan.finalproject.web;

import com.prusan.finalproject.web.command.Command;
import com.prusan.finalproject.web.command.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

/**
 * Main controller class.
 */
@WebServlet("/controller")
public class MainController extends HttpServlet {
    private static final Logger log = LogManager.getLogger(MainController.class);

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String com = req.getParameter("command");
        log.debug("got a command '{}'", com);
        Command command = CommandContainer.getCommand(com);
        command.execute(req, resp).moveForth(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String com = req.getParameter("command");
        log.debug("got a command '{}'", com);
        Command command = CommandContainer.getCommand(com);
        command.execute(req, resp).moveForth(req, resp);
    }
}