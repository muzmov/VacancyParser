package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class MoikrugStrategy extends AbstractVacancyStrategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=%s&page=%d";
    private static Logger logger = Logger.getLogger(getClassName());
    private static Map<String, Company> companies = new ConcurrentHashMap<>();

    @Override
    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        return getDocument(url);
    }

    @Override
    protected Document getDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("none")
                .get();
    }

    @Override
    protected String parseTitle(Element element) {
        return element.getElementsByAttributeValue("class", "title").text();
    }

    @Override
    protected String parseSalary(Element element) {
        return element.getElementsByAttributeValue("class", "salary").text();
    }

    @Override
    protected String parseCity(Element element) {
        return element.getElementsByAttributeValue("class", "location").text();
    }

    @Override
    protected String parseSiteName() {
        return "moikrug.ru";
    }

    @Override
    protected String parseUrl(Element element) {
        return "https://moikrug.ru" + element.select("a[class=job_icon]").attr("href");
    }

    @Override
    protected double parseRating(Element element) {
        return 0;
    }

    @Override
    protected Company parseCompany(Element element) {
        String companyName = element.getElementsByAttributeValue("class", "company_name").text();
        Company company;
        if ((company = companies.get(companyName)) == null) {
            company = new Company();
            company.setName(companyName);
            try {
                company.setUrl("https://moikrug.ru" + element.getElementsByAttributeValue("class", "company_name").get(0).children().attr("href"));
            } catch (Exception e) {
                logger.warn("Can't parse url for company " + companyName + ". " + e.getMessage());
            }

            company.setRating(0);
            company.setReviewsUrl("");

            companies.put(companyName, company);
        }
        return company;
    }

    @Override
    protected String parseDescription(Element element) {
        String url = parseUrl(element);
        String description = "";
        try {
            Document document = getDocument(url);
            description = document.getElementsByAttributeValue("class", "vacancy_description").text();
        } catch (IOException e) {
            logger.error("Can't get document for url = " + url + ". Exception: " + e.getMessage());
        }
        return description;
    }

    @Override
    protected Elements parseElements(Document doc) {
        return doc.getElementsByClass("job");
    }
}
