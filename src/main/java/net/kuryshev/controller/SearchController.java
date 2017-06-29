package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.Vacancy;
import net.kuryshev.model.VacancyParser;
import net.kuryshev.model.VacancyParserImpl;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.dao.VacancyDaoJdbc;
import net.kuryshev.model.strategy.HHStrategy;
import net.kuryshev.model.strategy.MoikrugStrategy;
import net.kuryshev.model.strategy.Provider;
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

    VacancyParser parser = new VacancyParserImpl(new Provider(new HHStrategy()), new Provider(new MoikrugStrategy()));

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchString = request.getParameter("searchString");
        logger.trace("Searching for \"" + searchString + "\"");
        List<Vacancy> searchResults = parser.selectCity("Moscow");
        request.setAttribute("vacancies", searchResults);
        logger.trace(searchResults.size() + " results found");
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
