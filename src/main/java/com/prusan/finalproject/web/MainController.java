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
        processRequest(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String com = req.getParameter("command");
        log.debug("got a command '{}'", com);
        Command command = CommandContainer.getCommand(com);
        log.debug("got a command instance {}", command);
        if (command == null) {
            req.getSession().setAttribute("err_msg", "Can not find this command");
        } else {
            command.execute(req, resp).moveForth(req, resp);
        }
    }
}