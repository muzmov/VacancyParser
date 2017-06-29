package net.kuryshev.model.strategy;

import net.kuryshev.model.Vacancy;
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
public class HHStrategy implements Strategy {
    private final static String URL_FORMAT =
            //"http://javarush.ru/testdata/big28data2.html?text=java+%s&page=%d";
            "http://hh.ru/search/vacancy?text=%s&page=%d";


    @Override
    public List<Vacancy> getVacancies(String searchString) {
        List<Vacancy> result = new ArrayList<>();
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
                    vacancy.setCity(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-address").text());
                    vacancy.setCompanyName(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text());
                    vacancy.setTitle(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").text());
                    vacancy.setSiteName("hh.ua");
                    vacancy.setUrl(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title").get(0).attr("href"));
                    if (element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation") != null)
                        vacancy.setSalary(element.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-compensation").text());
                    else
                        vacancy.setSalary("");
                   // System.out.println(vacancy);
                    result.add(vacancy);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
         //   break;
        }
        return result;
    }

    protected Document getDocument(String searchString, int page) throws IOException {
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
