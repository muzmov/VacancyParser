package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.model.VacancyParser;
import net.kuryshev.model.VacancyParserImpl;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.HhStrategy;
import net.kuryshev.model.strategy.MoikrugStrategy;
import net.kuryshev.model.strategy.Provider;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class AdminController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String searchString = request.getParameter("searchString");
        boolean hh = request.getParameter("hh") != null;
        boolean moikrug = request.getParameter("moikrug") != null;
        int numProviders = (hh?1:0) + (moikrug?1:0);

        logger.debug(numProviders + " providers selected");

        if (numProviders == 0 || searchString == null || searchString.isEmpty()) {
            request.setAttribute("error", "You should input a non empty query and choose at least one option from sites");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Provider[] providers = new Provider[numProviders];
        int i = 0;
        if (hh) providers[i++] = new Provider(new HhStrategy());
        if (moikrug) providers[i] = new Provider(new MoikrugStrategy());

        VacancyParser parser = new VacancyParserImpl(providers);
        logger.debug("Searching for \"" + searchString + "\" in " + (moikrug?"moikrug ":"") + (hh?"hh ":""));
        List<Vacancy> searchResults = parser.searchContaining(searchString);
        request.setAttribute("vacancies", searchResults);
        logger.debug(searchResults.size() + " results found");
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }
}
