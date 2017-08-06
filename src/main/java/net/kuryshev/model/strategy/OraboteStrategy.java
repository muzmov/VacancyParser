package net.kuryshev.model.strategy;

import net.kuryshev.model.entity.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OraboteStrategy extends AbstractCompanyStrategy {
    private List<Proxy> proxies;
    private static final Random random = new Random();
    private static final int NUM_TRIES = 10;
    private Proxy proxy;

    private static final String COMPANY_RATING_URL = "https://orabote.top";
    private static final String COMPANY_RATING_SEARCH_URL = "https://orabote.top/feedback/search";

    public OraboteStrategy() {
        loadProxies();
    }

    private void loadProxies() {
        proxies = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("../webapps/VacancyParser/proxies.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                String host = line.split(":")[0];
                int port = Integer.parseInt(line.split(":")[1]);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                proxies.add(proxy);
            }
        }
        catch (Exception e) {
            logger.warn("Exception while reading proxies file. Will continue work without proxies.", e);
            proxies = new ArrayList<>();
        }
        proxies.add(null);
    }

    private void nextProxy() {
        proxy = proxies.get(random.nextInt(proxies.size()));
    }

    @Override
    void fillExternalCompanyInfo(Company company) {
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
        Element element = elements.first();
        company.setRating(getCompanyRating(element));
        company.setReviewsUrl(getCompanyRewiewsUrl(element));
    }


    Document getDocument(String url, String company) {
        Document document = null;
        for (int i = 0; i < NUM_TRIES; i++) {
            try {
                document = Jsoup.connect(url).
                        timeout(5000).
                        proxy(proxy).
                        userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0").
                        referrer(COMPANY_RATING_URL).
                        data("country", "1", "company_name", company).
                        post();
                logger.info("The page for company " + company + " was read with proxy " + proxy);
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
        company.setReviewsUrl("");
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
