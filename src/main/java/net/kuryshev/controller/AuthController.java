package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.dao.UserDao;
import net.kuryshev.model.entity.User;
import net.kuryshev.model.entity.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class AuthController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Inject("userDao")
    private UserDao dao;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (exit(req)) {
            req.getRequestDispatcher("login.jsp").forward(req, resp);
            return;
        }

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        User user = new User(login, password, UserRole.NONE);
        UserRole role = dao.getRole(user);

        switch (role) {
            case ADMIN:
                req.getSession().setAttribute("role", role);
                resp.sendRedirect("admin.jsp");
                break;
            case USER:
                req.getSession().setAttribute("role", role);
                resp.sendRedirect("index.jsp");
                break;
            default:
                req.setAttribute("error", "Incorrect login or password");
                req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
    }

    private boolean exit(HttpServletRequest request) throws ServletException, IOException {
        if (request.getParameter("exit") != null) {
            logger.info("User with role " + request.getSession().getAttribute("role") + " exits");
            request.getSession().removeAttribute("role");
            return true;
        }
        return false;
    }
}
