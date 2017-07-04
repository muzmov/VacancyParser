package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=%s&page=%d";
    private static Logger logger = Logger.getLogger(getClassName());
    private Map<String, Company> companies = new HashMap<>();

    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> Vacancies = new ArrayList<>();
        int page = 0;
        Document doc = null;

        //TODO handle all the pages (only 10 now)
        while (page < 10) {
            try {
                doc = getDocument(searchString, page);
            } catch (IOException e) {
                logger.error("Can't get document for search query = " + searchString + " and page = " + page + ". Exception: " + e.getMessage());
            }
            Elements elements = doc.getElementsByClass("job");
            if (elements.size() == 0) break;
            for (Element element : elements) {
                if (element != null) {
                    Vacancy vacancy = new Vacancy();
                    vacancy.setTitle(getTitle(element));
                    vacancy.setSiteName(getSiteName());
                    vacancy.setUrl(getUrl(element));
                    vacancy.setSalary(getSalary(element));
                    vacancy.setCity(getCity(element));
                    vacancy.setCompany(getCompany(element));
                    vacancy.setDescription(getDescription(element));
                    vacancy.setRating(getRating(element));
                    Vacancies.add(vacancy);
                }
            }
            page++;
        }
        return Vacancies;
    }

    private Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        return getDocument(url);
    }

    private Document getDocument(String url) throws IOException {
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("none")
                .get();
        return document;
    }

    private String getTitle(Element element) {
        return element.getElementsByAttributeValue("class", "title").text();
    }


    private String getSalary(Element element) {
        return element.getElementsByAttributeValue("class", "salary").text();
    }


    private String getCity(Element element) {
        return element.getElementsByAttributeValue("class", "location").text();
    }


    private String getSiteName() {
        return "moikrug.ru";
    }


    private String getUrl(Element element) {
        return "https://moikrug.ru" + element.select("a[class=job_icon]").attr("href");
    }


    private double getRating(Element element) {
        return 0;
    }

    private Company getCompany(Element element) {
        String companyName = element.getElementsByAttributeValue("class", "company_name").text();
        Company company;
        if ((company = companies.get(companyName)) == null) {
            company = new Company();
            company.setName(companyName);
            company.setUrl("https://moikrug.ru" + element.getElementsByAttributeValue("class", "company_name").attr("href"));

            //TODO: set rewiewsUrl and rating
            company.setRating(0);
            company.setRewiewsUrl("");

            companies.put(companyName, company);
        }
        return company;
    }

    private String getDescription(Element element) {
        String url = getUrl(element);
        String description = "";
        try {
            Document document = getDocument(url);
            description = document.getElementsByAttributeValue("class", "vacancy_description").text();
        } catch (IOException e) {
            logger.error("Can't get document for url = " + url + ". Exception: " + e.getMessage());
        }
        return description;
    }


}
