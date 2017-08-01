package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.TaskProgress;
import net.kuryshev.model.VacancyParser;
import net.kuryshev.model.VacancyParserImpl;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.entity.Vacancy;
import net.kuryshev.model.strategy.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class AdminController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Inject("dao")
    private VacancyDao dao;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao.setProperties("../webapps/VacancyParser/WEB-INF/classes/dao.properties");
        if (delete(request) || parseReviews(request)) {
            request.getRequestDispatcher("admin.jsp").forward(request, response);
            return;
        }
        if (parseVacancies(request)) {
           // request.getRequestDispatcher("progress.jsp").forward(request, response);
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        }
        else {
            request.setAttribute("error", "You should choose a positive number of threads between 1 and 100, input a non empty query and choose at least one option from sites");
            request.setAttribute("goBackUrl", "admin.jsp");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private boolean delete(HttpServletRequest request) throws ServletException, IOException {
        if (request.getParameter("delete") != null) {
            logger.info("Delete all request accepted");
            dao.deleteAll();
            return true;
        }
        return false;
    }

    private boolean parseReviews(HttpServletRequest request) throws ServletException, IOException {
        if (request.getParameter("parsereviews") != null) {
            logger.info("Parse rewiews request accepted");
            TaskProgress progress = new TaskProgress();
            progress.setProgress("parsing...");
            request.getSession().setAttribute("ReviewsProgress", progress);
            new Thread(() -> {
                CompanyStrategy strategy = new OraboteStrategy();
                strategy.fillCompaniesInfo();
                progress.setDone(true);
                progress.setProgress("Done");
            }).start();
            return true;
        }
        return false;
    }

    private boolean parseVacancies(HttpServletRequest request) throws ServletException, IOException {
        int numThreads = getNumThreads(request);
        if (numThreads == -1) return false;

        String searchString = request.getParameter("searchString");
        Provider[] providers = getProviders(request, searchString, numThreads);
        if (providers == null) return false;

        TaskProgress progress = new TaskProgress();
        progress.setProgress("parsing...");
        request.getSession().setAttribute("ParsingProgress", progress);

        new Thread(() -> {
            VacancyParser parser = new VacancyParserImpl(providers);
            List<Vacancy> searchResults = parser.searchContaining(searchString);
            logger.info(searchResults.size() + " results found");
            progress.setProgress(searchResults.size() + " results found");
            progress.setDone(true);
        }).start();
        return true;
    }

    private int getNumThreads(HttpServletRequest request) throws ServletException, IOException {
        int numThreads = 0;
        try {
            numThreads = Integer.parseInt(request.getParameter("numThreads"));
        } catch (NumberFormatException e) {/*NOP*/}
        if (numThreads < 1 || numThreads > 100) return -1;
        else return numThreads;
    }

    private Provider[] getProviders(HttpServletRequest request, String searchString, int numThreads)
    throws ServletException, IOException {
        boolean hh = request.getParameter("hh") != null;
        boolean moikrug = request.getParameter("moikrug") != null;
        int numStrategies = (hh?1:0) + (moikrug?1:0);
        logger.debug(numStrategies + " providers selected");
        if (numStrategies == 0 || searchString == null || searchString.isEmpty()) {
            return null;
        }
        Provider[] providers = new Provider[numStrategies];
        int i = 0;
        if (hh) providers[i++] = new Provider(new HHStrategy(), numThreads);
        if (moikrug) providers[i] = new Provider(new MoikrugStrategy(), numThreads);
        logger.info("Searching for \"" + searchString + "\" in " + (moikrug?"moikrug ":"") + (hh?"hh ":""));
        return providers;
    }
}
