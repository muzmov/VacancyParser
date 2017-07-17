package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Random;

public class OraboteStrategy extends AbstractCompanyStrategy {
    //TODO: move to properties
    private static final Proxy[] proxies = {null, //connection without proxy
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("212.166.51.221", 80)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("46.150.172.128", 1080)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("85.143.218.246", 3128)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("77.73.64.12", 80)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("37.57.179.2", 8080)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("80.91.181.232", 3128)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("92.112.243.60", 3128)),
            new Proxy(Proxy.Type.HTTP, new InetSocketAddress("109.108.87.136", 53281)),
    };
    private static final Random random = new Random();
    private static final int NUM_TRIES = 10;
    private Proxy proxy;

    private static final String COMPANY_RATING_URL = "https://orabote.top";
    private static final String COMPANY_RATING_SEARCH_URL = "https://orabote.top/feedback/search";

    public OraboteStrategy() {
        //nextProxy();
       // proxy = proxies[3];
    }

    private void nextProxy() {
        proxy = proxies[random.nextInt(proxies.length)];
    }

    @Override
    void fillExternalCompanyInfo(Company company) {
        company.setRating(3.3);
        company.setRewiewsUrl("mock.url");
      //  setExternalCompanyInfo(company);
    }

    void setExternalCompanyInfo(Company company) {
        Document document = getDocument(COMPANY_RATING_SEARCH_URL, company.getName());
        if (document == null) {
            logger.warn(NUM_TRIES + " tries made. The site didn't respond");
            failedAttempts++;
            setDefaultExternalCompanyInfo(company);
            return;
        }
        Elements elements = document.getElementsByClass("news");
        if (elements.isEmpty()) {
            logger.warn("No info found for company " + company.getName());
            setDefaultExternalCompanyInfo(company);
            return;
        }
        //TODO: hanlde all the elements?
        Element element = elements.first();
        company.setRating(getCompanyRating(element));
        company.setRewiewsUrl(getCompanyRewiewsUrl(element));

    }

    Document getDocument(String url, String company) {
        Document document = null;
        for (int i = 0; i < NUM_TRIES; i++) {
            try {
                document = Jsoup.connect(url).
                        timeout(5000).
                        proxy(proxy).
                        userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0").
                        referrer("https://orabote.top/").
                        data("country", "1", "company_name", company).
                        post();
                logger.info("The page was read with proxy " + proxy);
                break;
            } catch (IOException e) {
                logger.warn("Can't get access to " + COMPANY_RATING_URL + " with proxy: " + proxy + ". " + e.getMessage());
                nextProxy();
            }
        }
        return document;
    }

    private void setDefaultExternalCompanyInfo(Company company) {
        company.setRating(0);
        company.setRewiewsUrl("");
    }

    private double getCompanyRating(Element element) {
        try {
            String ratingText = element.getElementsByClass("current-rating").text();
            String rating = ratingText.split(" ")[1].split("/")[0].replaceAll(",", ".");
            return Double.parseDouble(rating);
        } catch (Exception e) {
            logger.warn("Can't parse company rating. " + e.getMessage());
            return 0;
        }
    }

    private String getCompanyRewiewsUrl(Element element) {
        String relativeUrl = element.getElementsByClass("more-link").attr("href");
        if (relativeUrl == null || relativeUrl.isEmpty()) {
            logger.warn("Can't parse company url. ");
            return "";
        }
        return COMPANY_RATING_URL + relativeUrl;
    }
}
