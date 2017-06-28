package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.Vacancy;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


import static net.kuryshev.Utils.ClassUtils.getClassName;

/**
 * Created by 1 on 25.06.2017.
 */
public class SearchController extends DependencyInjectionServlet {
    Logger logger = Logger.getLogger(getClassName());

    @Inject("dao")
    VacancyDao dao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchString = request.getParameter("searchString");
        logger.trace("Searching for \"" + searchString + "\"");
        List<Vacancy> searchResults = dao.selectAll();
        request.setAttribute("vacancies", searchResults);
        logger.trace(searchResults.size() + " results found");
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
