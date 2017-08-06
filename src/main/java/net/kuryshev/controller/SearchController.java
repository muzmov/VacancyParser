package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.controller.di.Inject;
import net.kuryshev.model.SearchParams;
import net.kuryshev.model.comparators.ComparatorFactoryFactory;
import net.kuryshev.model.dao.VacancyDao;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class SearchController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Inject("dao")
    private VacancyDao dao;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("results.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String sortBy = request.getParameter("sortBy");
        Comparator<Vacancy> comparator = ComparatorFactoryFactory.getFactory(sortBy).getComparator();

        logger.info("Searching for \"" + searchString + "\" in "
                + (inTitle?"in title ":"") + (inDescription?"in description":""));
        List<Vacancy> searchResults = dao.selectContaining(searchString, params);
        if (comparator != null) searchResults.sort(comparator);
        request.setAttribute("vacancies", searchResults);
        logger.info(searchResults.size() + " results found");

        setRequestUrl(request);

        request.getRequestDispatcher("results.jsp").forward(request, response);
    }

    private void setRequestUrl(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        String requestString = "";
        for (Map.Entry<String, String[]> entry : params.entrySet()) {
            if (!entry.getKey().equals("sortBy")) requestString += entry.getKey() + "=" + entry.getValue()[0] + "&";
        }
        requestString = requestString.substring(0, requestString.length() - 1);
        request.setAttribute("requestString", requestString);
    }

}
