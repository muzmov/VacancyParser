package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.VacancyParser;
import net.kuryshev.model.VacancyParserImpl;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.strategy.HHStrategy;
import net.kuryshev.model.strategy.MoikrugStrategy;
import net.kuryshev.model.strategy.Provider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
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

    VacancyParser parser;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchString = request.getParameter("searchString");
        boolean inTitle = request.getParameter("title") != null;
        boolean inDescription = request.getParameter("description") != null;
        boolean hh = request.getParameter("hh") != null;
        boolean moikrug = request.getParameter("moikrug") != null;
        int numProviders = (hh?1:0) + (moikrug?1:0);
        int numPlacesToSearch = (inTitle?1:0) + (inDescription?1:0);

        logger.debug(numProviders + " providers and " + numPlacesToSearch + " places to search selected");

        if (numProviders == 0 || numPlacesToSearch == 0 || searchString == null || searchString.isEmpty()) {
            request.setAttribute("error", "You should input a non empty query and choose at least one option from sites and one option from where to search (title, description)");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Provider[] providers = new Provider[numProviders];
        int i = 0;
        if (hh) providers[i++] = new Provider(new HHStrategy());
        if (moikrug) providers[i++] = new Provider(new MoikrugStrategy());

        parser = new VacancyParserImpl(providers);
        logger.trace("Searching for \"" + searchString + "\" in " + (moikrug?"moikrug ":"") + (hh?"hh ":"") + (inTitle?"in title ":"") + (inDescription?"in description":""));
        List<Vacancy> searchResults = parser.searchContaining(searchString, inTitle, inDescription);
        request.setAttribute("vacancies", searchResults);
        logger.trace(searchResults.size() + " results found");
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }
}
