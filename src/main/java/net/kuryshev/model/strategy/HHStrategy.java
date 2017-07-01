package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import net.kuryshev.model.entity.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 08.06.2017.
 */
public class HHStrategy implements Strategy {
    private final static String URL_FORMAT =
            "http://hh.ru/search/vacancy?text=%s&page=%d";


    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> result = new ArrayList<>();
        Map<String, Company> companies = new HashMap<>();
        int page = 1;

        //TODO remove this page < 10 condition (it's here only for testing)
        while (page < 10) {
            try {
                Document doc = getDocument(searchString, page++);
                Elements elements = null;
                if (doc != null) {
                    elements = doc.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy");
                }
                if (elements == null || elements.isEmpty()) break;
                for (Element element : elements) {
                    Vacancy vacancy = new Vacancy();

                    //parsing vacancy
                    vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                    vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                    vacancy.setSiteName("hh.ru");
                    vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").get(0).attr("href"));
                    if (element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation") != null)
                        vacancy.setSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text());
                    else
                        vacancy.setSalary("");

                    //parsing description
                    //TODO: complex parsing
                    vacancy.setDescription(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_responsibility").text());

                    //parsing company
                    String companyName = element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text();
                    Company company;
                    if ((company = companies.get(companyName)) == null) {
                        company = new Company();
                        company.setName(companyName);
                        company.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").attr("href"));

                        //TODO: set rewiewsUrl and rating
                        company.setRating(0);
                        company.setRewiewsUrl("");

                        companies.put(companyName, company);
                    }

                    vacancy.setCompany(company);
                    result.add(vacancy);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Document getDocument(String searchString, int page) throws IOException {
        Document doc = null;
        String url = String.format(URL_FORMAT, searchString, page);
        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").referrer("none").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}
