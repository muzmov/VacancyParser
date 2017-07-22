package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.SearchParams;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class SearchController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Inject("dao")
    private VacancyDao dao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao.setProperties("../webapps/VacancyParser/WEB-INF/classes/dao.properties");
        String searchString = request.getParameter("searchString");
        boolean inTitle = request.getParameter("title") != null;
        boolean inDescription = request.getParameter("description") != null;
        int numPlacesToSearch = (inTitle?1:0) + (inDescription?1:0);

        SearchParams params = SearchParams.SELECT_ALL;
        if (numPlacesToSearch == 2) params = SearchParams.SEARCH_IN_TITLE_AND_DESCRIPTION;
        if (numPlacesToSearch == 1) {
            if (inTitle) params = SearchParams.SEARCH_IN_TITLE;
            if (inDescription) params = SearchParams.SEARCH_IN_DESCRIPTION;
        }

        logger.info("Searching for \"" + searchString + "\" in "
                + (inTitle?"in title ":"") + (inDescription?"in description":""));
        List<Vacancy> searchResults = dao.selectContaining(searchString, params);
        request.setAttribute("vacancies", searchResults);
        logger.info(searchResults.size() + " results found");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
