package net.kuryshev.model;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Proxy;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class ProxyChecker {
    private static Logger logger = Logger.getLogger(getClassName());

    private static final String URL = "http://yandex.ru";
    private static final int TIMEOUT = 2000;

    public static boolean isOk(Proxy proxy) {
        Document document;
        try {
            document = Jsoup.connect(URL).
                    timeout(TIMEOUT).
                    proxy(proxy).
                    userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64; rv:54.0) Gecko/20100101 Firefox/54.0").
                    referrer("").
                    get();
            if (document.getElementsByTag("title").text().isEmpty()) throw new IOException("Empty title");
            logger.info(proxy + " is OK");
        } catch (IOException e) {
            logger.info(proxy + " is not OK: " + e.getMessage());
            return false;
        }
        return true;
    }
}
