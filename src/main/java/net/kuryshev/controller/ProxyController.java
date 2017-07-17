package net.kuryshev.controller;

import net.kuryshev.controller.di.DependencyInjectionServlet;
import net.kuryshev.model.ProxyChecker;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

import static net.kuryshev.Utils.ClassUtils.getClassName;

public class ProxyController extends DependencyInjectionServlet {
    private Logger logger = Logger.getLogger(getClassName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String proxyParam = request.getParameter("proxies");
        logger.info("Parsing proxies: " + proxyParam);

        if (proxyParam == null || proxyParam.isEmpty()) {
            error(request, response);
            logger.warn("Proxy list is empty");
            return;
        }

        List<String> workingProxies = new ArrayList<>();

        String[] proxies = proxyParam.split("\n");
        try {
            for (String proxyString : proxies) {
                proxyString = proxyString.trim();
                String host = proxyString.split(":")[0];
                int port = Integer.parseInt(proxyString.split(":")[1]);
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
                if (ProxyChecker.isOk(proxy)) workingProxies.add(host + ":" + port + "\n");
            }
        } catch (Exception e) {
            logger.warn("Exception during proxies parse: " + e.getMessage());
            error(request, response);
        }
        if (!workingProxies.isEmpty()) {
            BufferedWriter bw = new BufferedWriter(new FileWriter("webapps/VacancyParser/proxies.txt"));
            for (String proxy : workingProxies) bw.write(proxy);
            bw.close();
        }
    }

    private void error(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("error", "You should input a non empty set of proxies. One proxy per line in host:port format with no additional symbols");
        request.getRequestDispatcher("error.jsp").forward(request, response);
    }

}
