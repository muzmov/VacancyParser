package net.kuryshev.filter;


import net.kuryshev.model.entity.UserRole;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static net.kuryshev.utils.ClassUtils.getClassName;

public class RoleFilter implements Filter {
    private Logger logger = Logger.getLogger(getClassName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        UserRole role = (UserRole) httpRequest.getSession().getAttribute("role");
        String url = httpRequest.getRequestURI();
        logger.info("User with role " + role + " is trying to open " + url + " page");

        if ((url.matches(".*admin.jsp.*") || url.matches(".*admin_parse.do.*")) && (role != UserRole.ADMIN)) {
            httpResponse.sendRedirect("login.jsp");
            return;
        }
/*        if ((url.matches(".*index.jsp.*") || url.matches(".*//*") || url.matches(".*search.do.*")) && role != UserRole.ADMIN && role != UserRole.USER) {
            httpResponse.sendRedirect("login.jsp");
            return;
        }*/
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
