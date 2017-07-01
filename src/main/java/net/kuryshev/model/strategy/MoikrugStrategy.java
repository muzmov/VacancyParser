package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 08.06.2017.
 */
public class MoikrugStrategy implements Strategy {
    private static final String URL_FORMAT = "https://moikrug.ru/vacancies?q=%s&page=%d";

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
                e.printStackTrace();
            }
            Elements vacancies = doc.getElementsByClass("job");
            if (vacancies.size() == 0) break;
            for (Element element : vacancies) {
                if (element != null) {
                    Vacancy vacancy = new Vacancy();

                    //parsing vacancy
                    vacancy.setTitle(element.getElementsByAttributeValue("class", "title").text());
                    vacancy.setSiteName(URL_FORMAT);
                    vacancy.setUrl("https://moikrug.ru" + element.select("a[class=job_icon]").attr("href"));
                    String salary = element.getElementsByAttributeValue("class", "salary").text();
                    String city = element.getElementsByAttributeValue("class", "location").text();
                    vacancy.setSalary(salary.length() == 0 ? "" : salary);
                    vacancy.setCity(city.length() == 0 ? "" : city);

                    //parsing company
                    //TODO: properly parse company
                    String companyName = element.getElementsByAttributeValue("class", "company_name").text();

                    Vacancies.add(vacancy);
                }
            }
            page++;
        }
        return Vacancies;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
        String url = String.format(URL_FORMAT, searchString, page);
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                .referrer("none")
                .get();
        return document;
    }
}
