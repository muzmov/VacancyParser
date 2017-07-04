package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class HhStrategy extends AbstractStrategy {
    private final static String URL_FORMAT = "http://hh.ru/search/vacancy?text=%s&page=%d";
    private static Logger logger = Logger.getLogger(getClassName());
    private Map<String, Company> companies = new HashMap<>();

    @Override
    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page + 1);
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
        return element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text();
    }

    @Override
    protected String parseSalary(Element element) {
        if (element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation") != null)
            return element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text();
        else return "";
    }

    @Override
    protected String parseCity(Element element) {
        return element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text();
    }

    @Override
    protected String parseSiteName() {
        return "hh.ru";
    }

    @Override
    protected String parseUrl(Element element) {
        return element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").attr("href");
    }

    @Override
    protected double parseRating(Element element) {
        return 0;
    }

    @Override
    protected Company parseCompany(Element element) {
        String companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text();
        Company company;
        if ((company = companies.get(companyName)) == null) {
            company = new Company();
            company.setName(companyName);
            company.setUrl("http://hh.ru" + element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").attr("href"));

            //TODO: set rewiewsUrl and rating
            company.setRating(0);
            company.setRewiewsUrl("");
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
            description = document.getElementsByAttributeValue("class", "b-vacancy-desc-wrapper").text();
        } catch (IOException e) {
            logger.error("Can't get document for url = " + url + ". Exception: " + e.getMessage());
        }
        return description;
    }

    @Override
    Elements parseElements(Document document) {
        return document.getElementsByAttributeValueMatching("data-qa", "vacancy-serp__vacancy( |$).*");
    }
}
