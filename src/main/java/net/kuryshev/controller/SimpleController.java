package net.kuryshev.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by 1 on 25.06.2017.
 */
public class SimpleController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("Title", "TitleFromAttrs");
        req.setAttribute("Body", "BodyFromAttrs");
        req.getRequestDispatcher("/index.jsp").include(req, resp);
    }
}
