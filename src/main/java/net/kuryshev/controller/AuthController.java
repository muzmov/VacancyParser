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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao.setProperties("../webapps/VacancyParser/WEB-INF/classes/dao.properties");
        if (exit(request)) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        User user = new User(login, password, UserRole.NONE);
        UserRole role = dao.getRole(user);

        switch (role) {
            case ADMIN:
                request.getSession().setAttribute("role", role);
                response.sendRedirect("admin.jsp");
                break;
            case USER:
                request.getSession().setAttribute("role", role);
                response.sendRedirect("index.jsp");
                break;
            default:
                request.setAttribute("error", "Incorrect login or password");
                request.getRequestDispatcher("error.jsp").forward(request, response);
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
