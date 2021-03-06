package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.kuryshev.utils.ClassUtils.getClassName;


public abstract class AbstractVacancyStrategy implements VacancyStrategy {
    private static Logger logger = Logger.getLogger(getClassName());
    private static final int MAX_PAGES = 20;


    @Override
    public List<Vacancy> getVacancies(String searchString, int initialPage, int jump) {
        List<Vacancy> Vacancies = new ArrayList<>();
        int page = initialPage;
        Document doc;

        while (page < MAX_PAGES) {
            try {
                doc = getDocument(searchString, page);
            } catch (IOException e) {
                logger.error("Can't get document for search query = " + searchString +
                        " and page = " + page, e);
                break;
            }
            Elements elements = parseElements(doc);
            if (elements.isEmpty()) {
                logger.debug("All pages (" + page + ") visited");
                break;
            }
            for (Element element : elements) {
                if (element != null) {
                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(parseTitle(element));
                    vacancy.setSiteName(parseSiteName());
                    vacancy.setUrl(parseUrl(element));
                    vacancy.setSalary(parseSalary(element));
                    vacancy.setCity(parseCity(element));
                    vacancy.setCompany(parseCompany(element));
                    vacancy.setDescription(parseDescription(element));
                    vacancy.setRating(parseRating(element));
                    Vacancies.add(vacancy);
                }
            }
            page += jump;
        }
        return Vacancies;
    }

    abstract Document getDocument(String searchString, int page) throws IOException;

    abstract Document getDocument(String url) throws IOException;

    abstract String parseTitle(Element element);

    abstract String parseSalary(Element element);

    abstract String parseCity(Element element);

    abstract String parseSiteName();

    abstract String parseUrl(Element element);

    abstract double parseRating(Element element);

    abstract Company parseCompany(Element element);

    abstract String parseDescription(Element element);

    abstract Elements parseElements(Document document);

}
